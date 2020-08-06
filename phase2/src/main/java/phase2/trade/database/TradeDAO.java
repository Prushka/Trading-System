package phase2.trade.database;

import org.hibernate.query.Query;
import phase2.trade.trade.Trade;
import phase2.trade.user.User;

import java.util.List;

public class TradeDAO extends  DAO<Trade>{

    public TradeDAO(DatabaseResourceBundle resource) {
        super(Trade.class, resource);
    }

    /*
    public List<Trade> findNumOfTransactions(String usernameOrEmail, String password) {
        Query query = getCurrentSession().createQuery("from Trade where (userName = :usernameOrEmail AND password = :password) OR (email = :usernameOrEmail AND password = :password)");
        query.setParameter("usernameOrEmail", usernameOrEmail);
        query.setParameter("password", password);
        return query.list();
    }

    public List<Trade> findNumOfBorrowing(String email) {
        Query query = getCurrentSession().createQuery("from User where email = :email");
        query.setParameter("email", email);
        return query.list();
    }

    public List<Trade> findNumOfLending(String userName) {
        Query query = getCurrentSession().createQuery("from User where userName = :userName");
        query.setParameter("userName", userName);
        return query.list();
    }
     */
}
