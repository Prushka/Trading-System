package group.phase2.rest;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.ArrayList;
import java.util.List;

public class SupportTicketRepository {

    List<SupportTicket> supportTickets = new ArrayList<>();

    public SupportTicketRepository(){
        supportTickets.add(new SupportTicket("demotitle","democontent"));
        supportTickets.add(new SupportTicket("demotitle2","democontent2"));
    }

    public List<SupportTicket> getSupportTickets() {
        return supportTickets;
    }


}
