package group.notification;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;

import java.util.Date;

public class SupportTicketManager {

    private final Repository<SupportTicket> repository;

    public SupportTicketManager(Repository<SupportTicket> repository) {
        this.repository = repository;
    }

    public Response getTicketsByCategory(SupportTicket.Category category) {
        Response response = repository.filterResponse(entity -> entity.getCategory() == category,
                (entity, builder) -> builder.response(ticketRepresentation("submit.ticket.category", entity)));
        return response;
    }

    private Response ticketRepresentation(String translatable, SupportTicket supportTicket) {
        return new Response.Builder(true).
                translatable(translatable,
                        new Date(supportTicket.getTimeStamp()), supportTicket.getContent(), supportTicket.getCategory(), supportTicket.getPriority(), supportTicket.getState())
                .build();
    }

    public Response addTicket(Request request) {
        SupportTicket supportTicket = new SupportTicket(request);
        repository.add(supportTicket);
        return ticketRepresentation("success.ticket", supportTicket);
    }
}
