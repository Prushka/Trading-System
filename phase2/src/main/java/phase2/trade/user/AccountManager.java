package phase2.trade.user;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.command.Login;
import phase2.trade.user.command.Register;

public class AccountManager {

    private User loggedInUser;

    private EntityBundle entityBundle;

    private Login loginCommand;

    private Register registerCommand;

    public AccountManager(EntityBundle entityBundle) {
        this.entityBundle = entityBundle;
        this.loginCommand = new Login(entityBundle);
        this.registerCommand = new Register(entityBundle);
    }

    public void login(Callback<User> callback, String usernameOrEmail, String password) {
        loginCommand.execute(result -> {
            loggedInUser = result;
            callback.call(result);
        }, usernameOrEmail, password);
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

    public void register(Callback<User> callback, String userName, String email, String password, String country, String city) {
        registerCommand.execute(result -> {
            loggedInUser = result;
            callback.call(result);
        }, userName, email, password, country, city);
    }
}
