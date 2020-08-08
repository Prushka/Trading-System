package phase2.trade.user;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.UserGateway;
import phase2.trade.user.command.Login;
import phase2.trade.user.command.Register;

import java.util.List;

public class AccountManager {

    private User loggedInUser;

    private GatewayBundle gatewayBundle;

    private Login loginCommand;

    private Register registerCommand;

    public AccountManager(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
        this.loginCommand = new Login(gatewayBundle);
        this.registerCommand = new Register(gatewayBundle);
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
