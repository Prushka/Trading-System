package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.notification.SupportTicket;
import group.notification.SupportTicketManager;
import group.repository.RepositorySavable;

public class SupportTicketController {

    private final SupportTicketManager supportTicketManager;

    private final RepositorySavable<SupportTicket> ticketRepository;

    public SupportTicketController(ControllerDispatcher dispatcher) {
        ticketRepository = dispatcher.ticketRepository;
        supportTicketManager = new SupportTicketManager(ticketRepository);
        dispatcher.menuConstructor.supportTicket(this);
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

}
