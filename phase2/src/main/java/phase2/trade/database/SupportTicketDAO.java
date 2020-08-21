package phase2.trade.database;

import phase2.trade.gateway.SupportTicketGateway;
import phase2.trade.support.SupportTicket;
import phase2.trade.user.User;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link SupportTicket} data access object.
 *
 * @author Dan Lyu
 * @see SupportTicket
 */
public class SupportTicketDAO extends DAO<SupportTicket, SupportTicketGateway> implements SupportTicketGateway {

    /**
     * Constructs a new Support ticket dao.
     *
     * @param databaseResourceBundle the database resource bundle
     */
    public SupportTicketDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(SupportTicket.class, databaseResourceBundle);
    }

    public List<SupportTicket> getUserSupportTickets(User operator) {
        final List<SupportTicket> result = new ArrayList<>();

        criteria((builder, query, root) -> {
            Predicate restriction = builder.equal(root.get("submitter"), operator);
            query.select(root).where(restriction);
            executeCriteriaQuery(result, query);
        });
        return result;
    }

    @Override
    protected SupportTicketGateway getThis() {
        return this;
    }
}

