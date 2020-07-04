package admin;

import menu.data.Response;
import repository.Repository;

public class TicketManager {

    private final Repository<Ticket> repository;

    public TicketManager(Repository<Ticket> repository) {
        this.repository = repository; // finish the repository interface & csv repo
    }

    public Response getTicketsByCategory(Ticket.Category category) {

        //TODO: Base Interface for Iterator
        return repository.filterResponse(entity -> entity.getCategory() == category,
                (entity, builder) -> builder.translatable("submit.ticket.category",
                        entity.getUid(), entity.getCategory(), entity.getContent()));
    }
}
