package phase2.trade.user;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.UserGateway;

import java.util.List;

public class AccountManager {

    private final UserGateway userGateway;

    private User loggedInUser;

    public AccountManager(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void register(Callback<User> callback, String username, String email, String password, String country,
                         String city) {
        userGateway.submitSessionWithTransactionAsync(() -> {
            List<User> usersByEmail = userGateway.findByEmail(email);
            List<User> usersByName = userGateway.findByEmail(email);
            if (usersByEmail.size() == 0 && usersByName.size() == 0) {
                User user = new PersonalUser(username, email, password, country, city);
                userGateway.add(user);
                loggedInUser = user;
                callback.call(user);
            }
            callback.call(null);
        });
    }

    public void login(Callback<User> callback, String usernameOrEmail, String password) {
        userGateway.submitSessionAsync(() -> {
            List<User> matchedUsers = userGateway.findMatches(usernameOrEmail, password);
            if (matchedUsers.size() > 0) {
                loggedInUser = matchedUsers.get(0);
                callback.call(loggedInUser);
            } else {
                callback.call(null);
            }
        });
    }

    public void logOut() {
        loggedInUser = null;
    }

    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
