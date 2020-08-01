package phase2.trade.controller;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import phase2.trade.database.UserDAO;
import phase2.trade.repository.Filter;
import phase2.trade.repository.Repository;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.List;
import java.util.logging.Level;

public class AccountManager {

    private final UserDAO userDAO;

    public AccountManager(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean register(String username, String email, String password) {
        boolean result;
        userDAO.openCurrentSessionWithTransaction();
        if (userDAO.findByEmail(email).size() > 0 && userDAO.findByUserName(username).size() > 0) {
            System.out.println("exists");
            result = false;
        } else {
            userDAO.add(new PersonalUser(username, email, password));
            result = true;
        }
        userDAO.closeCurrentSessionWithTransaction();
        return result;
    }

    public User login(String usernameOrEmail, String password) {
        userDAO.openCurrentSession();
        List<User> matchedUsers = userDAO.findMatches(usernameOrEmail, password);
        userDAO.closeCurrentSession();
        if (matchedUsers.size() > 0) {
            System.out.println("success");
            return matchedUsers.get(0);
        } else {
            System.out.println("failed");
            return null;
        }
    }

}
