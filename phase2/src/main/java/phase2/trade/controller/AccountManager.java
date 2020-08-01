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

import java.util.logging.Level;

public class AccountManager {

    private UserDAO userDAO;

    public AccountManager(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    public boolean register(String username, String email, String password) {
        boolean result;
        userDAO.openCurrentSessionWithTransaction();
        userDAO.add(new PersonalUser(username, email, password));
        if (userDAO.findByEmail(email) != null && userDAO.findByUserName(username) != null) {
            System.out.println("exists");
            result = false;
        } else {
            userDAO.add(new PersonalUser(username, email, password));
            result = true;
        }
        userDAO.closeCurrentSessionWithTransaction();
        return result;
    }

    public boolean login(String usernameOrEmail, String password) {
        if (userDAO.ifCredentialMatches(usernameOrEmail, password)) {
            System.out.println("success");
            return true;
        } else {
            System.out.println("failed");
            return false;
        }
    }

}
