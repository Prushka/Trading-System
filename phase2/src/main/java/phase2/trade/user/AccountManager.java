package phase2.trade.user;

import phase2.trade.database.Callback;
import phase2.trade.database.UserDAO;
import phase2.trade.item.Category;
import phase2.trade.item.Item;

import java.util.List;

public class AccountManager {

    private final UserDAO userDAO;

    private User loggedInUser;

    public AccountManager(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void register(Callback<User> callback, String username, String email, String password) {
        userDAO.submitSessionWithTransactionAsync(() -> {
            List<User> usersByEmail = userDAO.findByEmail(email);
            List<User> usersByName = userDAO.findByEmail(email);
            if (usersByEmail.size() == 0 && usersByName.size() == 0) {
                User user = new PersonalUser(username, email, password);
                userDAO.add(user);
                loggedInUser = user;
                callback.call(user);
            }
            callback.call(null);
        });
    }

    public void login(Callback<User> callback, String usernameOrEmail, String password) {
        userDAO.submitSessionAsync(() -> {
            List<User> matchedUsers = userDAO.findMatches(usernameOrEmail, password);
            if (matchedUsers.size() > 0) {
                loggedInUser = matchedUsers.get(0);
                callback.call(loggedInUser);
            } else {
                callback.call(null);
            }
        });
    }

    public void logOut(){
        loggedInUser = null;
    }

    public boolean isUserLoggedIn(){
        return loggedInUser!=null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
