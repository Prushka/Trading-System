package phase2.trade.controller;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import phase2.trade.database.Callback;
import phase2.trade.database.UserDAO;
import phase2.trade.repository.Filter;
import phase2.trade.repository.Repository;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class AccountManager {

    private final UserDAO userDAO;

    private User loggedInUser;

    private ExecutorService threadPool;

    public AccountManager(UserDAO userDAO) {
        this.userDAO = userDAO;
        threadPool = Executors.newFixedThreadPool(10);
    }

    public void register(Callback<User> callback, String username, String email, String password) {
        threadPool.submit(() -> {
            userDAO.openCurrentSessionWithTransaction();
            List<User> usersByEmail = userDAO.findByEmail(email);
            List<User> usersByName = userDAO.findByEmail(email);
            if (usersByEmail.size() == 0 && usersByName.size() == 0) {
                User user = new PersonalUser(username, email, password);
                userDAO.add(user);
                loggedInUser = user;
                callback.call(user);
            }
            callback.call(null);
            userDAO.closeCurrentSessionWithTransaction();
        });
    }

    public void login(Callback<User> callback, String usernameOrEmail, String password) {
        threadPool.submit(() -> {
            userDAO.openCurrentSession();
            List<User> matchedUsers = userDAO.findMatches(usernameOrEmail, password);
            userDAO.closeCurrentSession();
            if (matchedUsers.size() > 0) {
                loggedInUser = matchedUsers.get(0);
                callback.call(loggedInUser);
            } else {
                callback.call(null);
            }
        });
    }

}
