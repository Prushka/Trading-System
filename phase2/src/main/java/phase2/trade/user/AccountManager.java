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

    public void login(Callback<User> callback, String usernameOrEmail, String password) {
        userGateway.submitSession(() -> {
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
