package phase2.trade.gateway;

import phase2.trade.support.SupportTicket;
import phase2.trade.user.User;

import java.util.List;

/**
 * The {@link SupportTicket} gateway.
 *
 * @author Dan Lyu
 * @see SupportTicket
 */
public interface SupportTicketGateway extends EntityGateway<SupportTicket, SupportTicketGateway> {

    /**
     * Gets user support tickets.
     *
     * @param operator the operator
     * @return the user support tickets
     */
    List<SupportTicket> getUserSupportTickets(User operator);
}
