package phase2.trade.database;

import org.hibernate.query.Query;
import phase2.trade.user.User;

import java.util.List;

public class AdministrativeUserDAO extends UserDAO{

    public AdministrativeUserDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(databaseResourceBundle);
    }

    public List<User> findMatches(String usernameOrEmail, String password) {
        Query query = getCurrentSession().createQuery("from User where (userName = :usernameOrEmail AND password = :password) OR (email = :usernameOrEmail AND password = :password)");
        query.setParameter("usernameOrEmail", usernameOrEmail);
        query.setParameter("password", password);
        return query.list();
    }

    public List<User> findByEmail(String email) {
        Query query = getCurrentSession().createQuery("from User where email = :email");
        query.setParameter("email", email);
        return query.list();
    }

    public List<User> findByUserName(String userName) {
        Query query = getCurrentSession().createQuery("from User where userName = :userName");
        query.setParameter("userName", userName);
        return query.list();
    }

        /*public List<User> findByUserFreeze(String userName) {
            Query query = getCurrentSession().createQuery("from User where lendCount < :borrowCount");
            query.setParameter("lendCount", lendCount);
            query.setParameter("borrowCount", borrowCount);
            return query.list();*/
}
