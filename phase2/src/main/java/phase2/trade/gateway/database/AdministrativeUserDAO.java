package phase2.trade.gateway.database;

import org.hibernate.query.Query;
import phase2.trade.user.User;

import java.util.List;

public class AdministrativeUserDAO extends UserDAO{

    public AdministrativeUserDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(databaseResourceBundle);
    }

    @Override
    public List<User> findMatches(String usernameOrEmail, String password) {
        Query query = getCurrentSession().createQuery("from AdministrativeUser where (userName = :usernameOrEmail AND password = :password) OR (email = :usernameOrEmail AND password = :password)");
        query.setParameter("usernameOrEmail", usernameOrEmail);
        query.setParameter("password", password);
        return query.list();
    }

    @Override
    public List<User> findByEmail(String email) {
        Query query = getCurrentSession().createQuery("from AdministrativeUser where email = :email");
        query.setParameter("email", email);
        return query.list();
    }

    @Override
    public List<User> findByUserName(String userName) {
        Query query = getCurrentSession().createQuery("from AdministrativeUser where userName = :userName");
        query.setParameter("userName", userName);
        return query.list();
    }

    @Override
    public List<User> findByCity(String city) {
        Query query = getCurrentSession().createQuery("from AdministrativeUser where address.city = :city");
        query.setParameter("city", city);
        return query.list();
    }

        /*public List<User> findByUserFreeze(String userName) {
            Query query = getCurrentSession().createQuery("from User where lendCount < :borrowCount");
            query.setParameter("lendCount", lendCount);
            query.setParameter("borrowCount", borrowCount);
            return query.list();*/
}
