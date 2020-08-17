package phase2.trade.database;

import phase2.trade.gateway.SupportTicketGateway;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.support.SupportTicket;
import phase2.trade.user.AccountState;
import phase2.trade.user.User;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SupportTicketDAO extends DAO<SupportTicket, SupportTicketGateway> implements SupportTicketGateway {

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

