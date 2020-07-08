package group.notification;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;

public class SupportTicketManager {

    private final Repository<SupportTicket> repository;

    public SupportTicketManager(Repository<SupportTicket> repository) {
        this.repository = repository; // finish the group.repository interface & csv repo
    }

    public Response getTicketsByCategory(SupportTicket.Category category) {
        return repository.filterResponse(entity -> entity.getCategory() == category,
                (entity, builder) -> builder.translatable("submit.ticket.category",
                        entity.getUid(), entity.getCategory(), entity.getContent()));
    }

    public Response addTicket(Request request) {
        SupportTicket supportTicket = new SupportTicket(request);
        if (repository.ifExists(supportTicket)) {
            return new Response.Builder().success(false).translatable("exists.ticket").build();
        } else {
            repository.add(supportTicket);
            return new Response.Builder().success(true).translatable("success.ticket").build();
        }
    }
}
