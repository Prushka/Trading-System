package system;

import admin.Ticket;
import admin.TicketManager;
import menu.Menu;
import menu.data.Request;
import menu.data.Response;
import menu.node.*;
import menu.validator.EmailValidator;
import menu.validator.Validator;
import repository.Repository;
import repository.SerializableRepository;

public class TestController {

    private final TicketManager ticketManager;
    private final Repository<Ticket> ticketRepository;

    public TestController() {
        ticketRepository =
                new SerializableRepository<>("a.ser");
        Ticket ticket = new Ticket("123", Ticket.Category.ACCOUNT);
        Ticket ticket1 = new Ticket("234", Ticket.Category.TRADE);
        Ticket ticket2 = new Ticket("345", Ticket.Category.BOOK);
        Ticket ticket3 = new Ticket("456", Ticket.Category.ACCOUNT);
        ticketRepository.add(ticket);
        ticketRepository.add(ticket1);
        ticketRepository.add(ticket2);
        ticketRepository.add(ticket3);
        // ticketRepository.add(ticket);

        // ticketRepository.save();
        //System.out.println(ticketRepository.get(0).getCategory());
        //repository.add(ticket);
        //repository.save();

        //System.out.println(ticketRepository.get(0).get("content"));

        //Ticket ticket = new Ticket();
        //ticket.setContent("aha");
        //ticketRepository.addTicket(ticket);
        //ticketRepository.save();
        ticketManager = new TicketManager(ticketRepository);


        OptionNode registerOptionNode = new OptionNode.Builder("option.register").id(1).build();
        OptionNode loginOptionNode = new OptionNode.Builder("option.login").id(2).build();

        OptionNode ticketTest = new OptionNode.Builder("option.ticket").id(3).build();
        SubmitNode ticketSubmit = new SubmitNode.Builder("input.ticket.category").validator(input -> {
            try {
                Ticket.Category.valueOf(input.toUpperCase());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }, new ResponseNode("invalid.ticket.category"))
                .inputProcessor(String::toUpperCase)
                .submitHandler(this::getTicketResponse).build();

        MasterOptionNode accountMaster = new MasterOptionNode.Builder("account.master")
                .addChild(registerOptionNode, loginOptionNode, ticketTest).build();

        InputNode emailInput = new InputNode.Builder("input.email")
                .validator(new EmailValidator(),new ResponseNode("invalid.email"))
                .build();

        InputNode userNameInput = new InputNode.Builder("input.username").build();


        registerOptionNode.setChild(emailInput).setChild(userNameInput);

        ticketTest.setChild(ticketSubmit);

        Menu menu = new Menu(accountMaster);
        ConsoleSystem console = new ConsoleSystem();
        console.run(menu);
    }

    public Response getTicketResponse(Request request) {
        Response.Builder builder = new Response.Builder();
        for (Ticket t : ticketManager.getTicketsByCategory(Ticket.Category.valueOf(request.get("input.ticket.category")))) {
            builder.translatable("submit.ticket.category",0, t.getCategory(), t.getContent());
        }
        return builder.build();
    }

    public void shutdown() {
        ticketRepository.save();
    }
}
