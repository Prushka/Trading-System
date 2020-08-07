package phase2.trade.gateway.database;

import org.hibernate.Criteria;
import org.hibernate.query.Query;
import phase2.trade.gateway.TradeGateway;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeState;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.user.User;

import java.util.List;

public class TradeDAO extends DAO<Trade> implements TradeGateway {

    public TradeDAO(DatabaseResourceBundle resource) {
        super(Trade.class, resource);
    }

    // I'm not sure if these will work or if you can use function calls in query strings
    public int findNumOfTransactions(User currUser) {
        Query query = getCurrentSession().createQuery("select count(T.order.getUsers(:currUser)) from Trade as T where tradeState = :tradeState");
        query.setParameter("tradeState", TradeState.CLOSED);
        query.setParameter("currUser", currUser);
        return ((int) query.list().get(0));
    }

    public int findNumOfBorrowing(User currUser) {
        Query query = getCurrentSession().createQuery("select count(T.order.borrowed(:currUser)) from Trade as T where tradeState = :tradeState");
        query.setParameter("tradeState", TradeState.CLOSED);
        query.setParameter("currUser", currUser);
        return ((int) query.list().get(0));
    }

    public int findNumOfLending(User currUser) {
        Query query = getCurrentSession().createQuery("select count(T.order.lent(:currUser)) from Trade as T where tradeState = :tradeState");
        query.setParameter("tradeState", TradeState.CLOSED);
        query.setParameter("currUser", currUser);
        return ((int) query.list().get(0));
    }
}
