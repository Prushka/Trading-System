package group.notification;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.user.PersonalUser;

import java.util.Date;

public class SupportTicketManager {

    private final Repository<SupportTicket> supportTicketRepository;
    private final Repository<PersonalUser> personalUserRepository;

    public SupportTicketManager(Repository<SupportTicket> supportTicketRepository, Repository<PersonalUser> personalUserRepository) {
        this.supportTicketRepository = supportTicketRepository;
        this.personalUserRepository = personalUserRepository;
    }

    public Response getTicketsByCategory(SupportTicket.Category category) {
        return supportTicketRepository.filterResponse(entity -> entity.getCategory() == category,
                (entity, builder) -> builder.response(ticketRepresentation("submit.ticket.category", entity)));
    }

    private Response ticketRepresentation(String translatable, SupportTicket supportTicket) {
        return new Response.Builder(true).
                translatable(translatable,
                        new Date(supportTicket.getTimeStamp()), supportTicket.getContent(), supportTicket.getCategory(), supportTicket.getPriority(), supportTicket.getState())
                .build();
    }

    public Response addTicket(Request request) {
        SupportTicket supportTicket = new SupportTicket(request);
        supportTicketRepository.add(supportTicket);
        return ticketRepresentation("success.ticket", supportTicket);
    }

    public boolean ifTicketContentNotExist(String input) {
        return !supportTicketRepository.ifExists(entity -> input.equalsIgnoreCase(entity.getContent()));
    }

    public Response getAllTickets() {
        return supportTicketRepository.filterResponse(
                (entity, builder) -> builder.response(ticketRepresentation("submit.ticket.category", entity)));
    }
}
