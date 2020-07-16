package group.phase2.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SupportTicketController {

    @Autowired
    SupportTicketManager supportTicketManager;

    @GetMapping("/support/tickets")
    public List<SupportTicket> getAllSupportTickets() {
        return supportTicketManager.getAllSupportTickets();
    }

    @GetMapping("/support/tickets/{id}")
    public SupportTicket getSupportTicketById(@PathVariable("id") String id) {
        return supportTicketManager.getSupportTicketById(id);
    }

    @GetMapping("/support/tickets2")
    public ResponseEntity<SupportTicket> getSupportTicketById2(@RequestParam(value = "id") String id) {
        SupportTicket supportTicket = supportTicketManager.getSupportTicketById(id);
        if (supportTicket == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(supportTicket, HttpStatus.OK);
    }
}
