package group.phase2.rest;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportTicketManager {

    SupportTicketRepository supportTicketRepository = new SupportTicketRepository();

    public List<SupportTicket> getAllSupportTickets(){
        return supportTicketRepository.getSupportTickets();
    }

    public SupportTicket getSupportTicketById(String id) {
        return supportTicketRepository.getSupportTickets().get(Integer.parseInt(id));
    }

}
