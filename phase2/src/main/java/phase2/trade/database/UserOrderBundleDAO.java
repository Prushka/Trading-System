package phase2.trade.database;

import phase2.trade.gateway.SupportTicketGateway;
import phase2.trade.gateway.UserOrderBundleGateway;
import phase2.trade.support.SupportTicket;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.user.User;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserOrderBundleDAO extends DAO<UserOrderBundle, UserOrderBundleGateway> implements UserOrderBundleGateway {

    public UserOrderBundleDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(UserOrderBundle.class, databaseResourceBundle);
    }

    public int getUserBorrowCount() {
        return 0;
    }

    public int getUserPurchaseCount() {
        return 0;
    }

    @Override
    protected UserOrderBundleGateway getThis() {
        return this;
    }
}

