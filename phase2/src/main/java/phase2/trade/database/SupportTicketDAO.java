package phase2.trade.database;

import phase2.trade.gateway.SupportTicketGateway;
import phase2.trade.support.SupportTicket;

public class SupportTicketDAO extends DAO<SupportTicket, SupportTicketGateway> implements SupportTicketGateway {

    public SupportTicketDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(SupportTicket.class, databaseResourceBundle);
    }

    @Override
    protected SupportTicketGateway getThis() {
        return this;
    }
}

