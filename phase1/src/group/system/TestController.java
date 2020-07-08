package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
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
        MenuConstructor menu = new MenuConstructor(this);
    }

    public Response addTicket(Request request) {
        return supportTicketManager.addTicket(request);
    }

    public Response getTicketsByCategory(Request request) {
        return supportTicketManager.getTicketsByCategory(SupportTicket.Category.valueOf(request.get("category")));
    }

    public boolean ifTicketContentNotExist(String input) {
        return !ticketRepository.ifExists(entity -> input.equalsIgnoreCase(entity.getContent()));
    }

    public void shutdown() {
        ticketRepository.save();
    }
}
