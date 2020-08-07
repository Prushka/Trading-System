package phase2.trade.database;

import org.hibernate.query.Query;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.List;

public class PersonalUserDAO extends UserDAO {

    public PersonalUserDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(databaseResourceBundle);
    }

    @Override
    public List<User> findMatches(String usernameOrEmail, String password) {
        Query query = getCurrentSession().createQuery("from PersonalUser where (userName = :usernameOrEmail AND password = :password) OR (email = :usernameOrEmail AND password = :password)");
        query.setParameter("usernameOrEmail", usernameOrEmail);
        query.setParameter("password", password);
        return query.list();
    }

    @Override
    public List<User> findByEmail(String email) {
        Query query = getCurrentSession().createQuery("from PersonalUser where email = :email");
        query.setParameter("email", email);
        return query.list();
    }

    @Override
    public List<User> findByUserName(String userName) {
        Query query = getCurrentSession().createQuery("from PersonalUser where userName = :userName");
        query.setParameter("userName", userName);
        return query.list();
    }

    @Override
    public List<User> findByCity(String city) {
        Query query = getCurrentSession().createQuery("from PersonalUser where address.city = :city");
        query.setParameter("city", city);
        return query.list();
    }


    /*public List<PersonalUser> findByUserFreeze() {
        return super.findAll();
    }*/
}



