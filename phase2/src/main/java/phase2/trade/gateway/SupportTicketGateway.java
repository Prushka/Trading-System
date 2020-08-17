package phase2.trade.gateway;

import phase2.trade.support.SupportTicket;
import phase2.trade.user.User;

import java.util.List;

public interface SupportTicketGateway extends EntityGateway<SupportTicket, SupportTicketGateway> {

    List<SupportTicket> getUserSupportTickets(User operator);
}
