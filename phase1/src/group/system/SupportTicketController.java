package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.notification.SupportTicket;
import group.notification.SupportTicketManager;
import group.repository.Repository;

public class SupportTicketController {

    private final SupportTicketManager supportTicketManager;

    public SupportTicketController(ControllerDispatcher dispatcher) {
        supportTicketManager = new SupportTicketManager(dispatcher.ticketRepository, dispatcher.personalUserRepository);
        dispatcher.menuController.supportTicket(this);
    }

    public Response addTicket(Request request) {
        return supportTicketManager.addTicket(request);
    }

    public Response getTicketsByCategory(Request request) {
        return supportTicketManager.getTicketsByCategory(SupportTicket.Category.valueOf(request.get("category")));
    }

    public Response getAllTickets(Request request){
        return supportTicketManager.getAllTickets();
    }

    public boolean ifTicketContentNotExist(String input) {
        return supportTicketManager.ifTicketContentNotExist(input);
    }

}
