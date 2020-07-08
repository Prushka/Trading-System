package group.system;

import group.menu.Menu;
import group.menu.data.Request;
import group.menu.data.Response;
import group.menu.handler.RequestHandler;
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

        OptionNode supportTicketCreationNode = new OptionNode("option.support.ticket.add", 1);
        OptionNode supportTicketLookupNode = new OptionNode("option.support.ticket.lookup", 2);

        MasterOptionNode masterOptionNode = new MasterOptionNode("master.option.ticket",
                supportTicketCreationNode, supportTicketLookupNode);

        InputNode inputTicketContent = new InputNode.Builder("input.ticket.content", "content").build();
        InputNode inputTicketCategory = new InputNode.Builder("input.ticket.category", "category")
                .validator(new EnumValidator<>(SupportTicket.Category.class), "invalid.ticket.category")
                .inputProcessor(String::toUpperCase)
                .build();

        SubmitNode inputTicketPriority = new SubmitNode.Builder("input.ticket.priority",
                "priority", this::addTicket)
                .validator(new EnumValidator<>(SupportTicket.Priority.class), "invalid.ticket.priority")
                .inputProcessor(String::toUpperCase).build();

        SubmitNode inputTicketCategoryLookup = new SubmitNode.Builder("input.ticket.category.lookup", "category"
                , new RequestHandler() {
            @Override
            public Response handle(Request request) {
                return getTicketsByCategory(request); // this is the same as this::getTicketsByCategory
            }
        })
                .validator(new EnumValidator<>(SupportTicket.Category.class), "invalid.ticket.category")
                .inputProcessor(String::toUpperCase).build();

        supportTicketCreationNode.setChild(inputTicketContent).setChild(inputTicketCategory).setChild(inputTicketPriority);
        supportTicketLookupNode.setChild(inputTicketCategoryLookup);

        Menu menu = new Menu(masterOptionNode);
        ConsoleSystem console = new ConsoleSystem();
        console.run(menu);
    }

    public Response addTicket(Request request) {
        return supportTicketManager.addTicket(request);
    }

    public Response getTicketsByCategory(Request request) {
        return supportTicketManager.getTicketsByCategory(SupportTicket.Category.valueOf(request.get("category")));
    }

    public void shutdown() {
        ticketRepository.save();
    }
}
