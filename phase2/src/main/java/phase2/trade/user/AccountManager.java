package phase2.trade.user;

import phase2.trade.callback.Callback;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CommandFactory;
import phase2.trade.gateway.ConfigBundle;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionGroupFactory;
import phase2.trade.user.command.CreateUser;
import phase2.trade.user.command.Login;

public class AccountManager {

    private User loggedInUser;

    private EntityBundle entityBundle;

    private final Login loginCommand;

    private final CreateUser registerCommand;

    private final User system;

    private final UserFactory userFactory;

    public AccountManager(GatewayBundle gatewayBundle) {
        userFactory = new UserFactory(gatewayBundle.getConfigBundle().getPermissionConfig());
        CommandFactory commandFactory = new CommandFactory(gatewayBundle, this);
        system = userFactory.configureSystemUser();
        this.loginCommand = commandFactory.getCommand(Login::new, false);
        this.registerCommand = commandFactory.getCommand(CreateUser::new, true);
    }

    public void loginAsGuest() {
        loggedInUser = userFactory.configureGuest();
    }

    public void login(StatusCallback<User> callback, String usernameOrEmail, String password) {
        loginCommand.execute((result, status) -> {
            loggedInUser = result;
            callback.call(result, status);
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

    public PermissionGroup getPermissionGroup() {
        return loggedInUser.getPermissionGroup();
    }

    public void register(StatusCallback<User> callback, String userName, String email, String password, String country, String city) {
        registerCommand.execute((result, status) -> {
            loggedInUser = result;
            callback.call(result, status);
        }, userName, email, password, country, city, PermissionGroup.REGULAR.name());
    }
}
