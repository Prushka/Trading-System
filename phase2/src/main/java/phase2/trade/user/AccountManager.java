package phase2.trade.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.command.CommandFactory;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.user.command.CreateUser;
import phase2.trade.user.command.Login;

public class AccountManager {

    private static final Logger logger = LogManager.getLogger(AccountManager.class);

    private User loggedInUser;

    private final Login loginCommand;

    private final CreateUser registerCommand;

    private final UserFactory userFactory;

    public AccountManager(GatewayBundle gatewayBundle) {
        userFactory = new UserFactory(gatewayBundle.getConfigBundle().getPermissionConfig());
        CommandFactory commandFactory = new CommandFactory(gatewayBundle, this);
        this.loginCommand = commandFactory.getCommand(Login::new, true);
        this.registerCommand = commandFactory.getCommand(CreateUser::new, true);
    }

    public void loginAsGuest() {
        loggedInUser = userFactory.configureGuest();
    }

    public void login(ResultStatusCallback<User> callback, String usernameOrEmail, String password) {
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
        if (loggedInUser == null) {
            logger.warn("The logged in user is missing but someone is trying to pull an existing logged in user!");
            logger.warn("If this happens when you try to mock the dashboard, " +
                    "please check the database hbm2ddl option, if you are logging using wrong credentials or if you are mocking the registration" +
                    "of a user who" +
                    "already exists in the database!");
        }
        return loggedInUser;
    }

    public PermissionGroup getPermissionGroup() {
        return loggedInUser.getPermissionGroup();
    }

    public void register(ResultStatusCallback<User> callback, String userName, String email, String password, String country, String province, String city) {
        registerCommand.execute((result, status) -> {
            loggedInUser = result;
            callback.call(result, status);
        }, userName, email, password, PermissionGroup.REGULAR.name(), country, province, city);
    }
}
