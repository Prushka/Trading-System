package group.system;

import group.menu.Menu;
import group.menu.data.Request;
import group.menu.data.Response;
import group.menu.node.InputNode;
import group.menu.node.MasterOptionNode;
import group.menu.node.OptionNode;
import group.menu.node.SubmitNode;
import group.menu.validator.EnumValidator;
import group.notification.SupportTicket;
import group.notification.SupportTicketManager;
import group.repository.CSVRepository;
import group.repository.Repository;

public class TestController {

    private final SupportTicketManager supportTicketManager;
    private final Repository<SupportTicket> ticketRepository;

    public TestController() {
        ticketRepository =
                new CSVRepository<>("data/support_ticket.csv", SupportTicket::new);
        supportTicketManager = new SupportTicketManager(ticketRepository);

        OptionNode supportTicketCreationNode =
                new OptionNode.Builder("option.support.ticket").id(1).build();

        MasterOptionNode masterOptionNode = new MasterOptionNode.Builder("master.option.ticket")
                .addChild(supportTicketCreationNode).build();

        InputNode inputTicketContent = new InputNode.Builder("input.ticket.content","content").build();

        InputNode inputTicketCategory = new InputNode.Builder("input.ticket.category","category")
                .validator(new EnumValidator<>(SupportTicket.Category.class),"invalid.ticket.category")
                .inputProcessor(String::toUpperCase)
                .build();

        SubmitNode inputTicketPriority = new SubmitNode.Builder("input.ticket.priority","priority")
                .validator(new EnumValidator<>(SupportTicket.Priority.class),"invalid.ticket.priority")
                .inputProcessor(String::toUpperCase)
                .submitHandler(this::addTicket).build();

        supportTicketCreationNode.setChild(inputTicketContent).setChild(inputTicketCategory).setChild(inputTicketPriority);

        Menu menu = new Menu(masterOptionNode);
        ConsoleSystem console = new ConsoleSystem();
        console.run(menu);
    }

    public Response addTicket(Request request){
        return supportTicketManager.addTicket(request);
    }

    public void shutdown() {
        ticketRepository.save();
    }
}
