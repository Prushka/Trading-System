package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.notification.SupportTicket;
import group.notification.SupportTicketManager;
import group.repository.Repository;

public class SupportTicketController {

    private final SupportTicketManager supportTicketManager;

    private final Repository<SupportTicket> ticketRepository;

    public SupportTicketController(ControllerDispatcher dispatcher) {
        ticketRepository = dispatcher.ticketRepository;
        supportTicketManager = new SupportTicketManager(ticketRepository);
        dispatcher.menuController.supportTicket(this);
    }

    public Response addTicket(Request request) {
        return supportTicketManager.addTicket(request);
    }

    public Response getTicketsByCategory(Request request) {
        return supportTicketManager.getTicketsByCategory(SupportTicket.Category.valueOf(request.get("category")));
    }

    public Response getTicketsByCategory2(Request request) {
        return new Response(true,"123456");
    }

    public boolean ifTicketContentNotExist(String input) {
        return !ticketRepository.ifExists(entity -> input.equalsIgnoreCase(entity.getContent()));
    }

}
