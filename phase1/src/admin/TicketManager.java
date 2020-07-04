package admin;

import repository.Repository;
import repository.RepositoryIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TicketManager {

    private final Repository<Ticket> repository;

    public TicketManager(Repository<Ticket> repository) {
        this.repository = repository;
    }

    public List<Ticket> getTicketsByCategory(Ticket.Category category) {
        Iterator<Ticket> iterator = repository.iterator();
        List<Ticket> tickets = new ArrayList<>();
        while (iterator.hasNext()) { // is an iterator really necessary for this?
            Ticket ticket = iterator.next();
            if (ticket.getCategory() == category) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }
}
