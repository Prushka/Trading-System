package phase2.trade.user;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.UserGateway;
import phase2.trade.user.command.Login;
import phase2.trade.user.command.Register;

import java.util.List;

public class LoginManager {

    private User loggedInUser;

    private GatewayBundle gatewayBundle;

    private Login loginCommand;

    private Register registerCommand;

    public LoginManager(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
        this.loginCommand = new Login(gatewayBundle);
    }

    public void login(String usernameOrEmail, String password) {
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
