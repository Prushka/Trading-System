package phase2.trade.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.avatar.Avatar;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.command.CommandFactory;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.user.command.CreateUserOperation;
import phase2.trade.user.command.Login;

/**
 * The Account manager.
 *
 * @author Dan Lyu
 */
public class AccountManager {

    private static final Logger logger = LogManager.getLogger(AccountManager.class);

    private User loggedInUser;

    private final Login loginCommand;

    private final CreateUserOperation registerCommand;

    private final UserFactory userFactory;

    /**
     * Constructs a new Account manager.
     *
     * @param gatewayBundle the gateway bundle
     */
    public AccountManager(GatewayBundle gatewayBundle) {
        userFactory = new UserFactory(gatewayBundle.getConfigBundle().getPermissionConfig());
        CommandFactory commandFactory = new CommandFactory(gatewayBundle, this);
        this.loginCommand = commandFactory.getCommand(Login::new, true);
        this.registerCommand = commandFactory.getCommand(CreateUserOperation::new, true);
    }

    /**
     * Login as guest.
     */
    public void loginAsGuest() {
        loggedInUser = userFactory.configureGuest();
    }

    /**
     * Login.
     *
     * @param callback        the callback
     * @param usernameOrEmail the username or email
     * @param password        the password
     */
    public void login(ResultStatusCallback<User> callback, String usernameOrEmail, String password) {
        loginCommand.execute((result, status) -> {
            loggedInUser = result;
            callback.call(result, status);
        }, usernameOrEmail, password);
    }


    /**
     * Log out.
     */
    public void logOut() {
        loggedInUser = null;
    }

    /**
     * Is user logged in boolean.
     *
     * @return the boolean
     */
    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }

    /**
     * Gets logged in user.
     *
     * @return the logged in user
     */
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

    /**
     * Gets permission group.
     *
     * @return the permission group
     */
    public PermissionGroup getPermissionGroup() {
        return loggedInUser.getPermissionGroup();
    }

    /**
     * Register.
     *
     * @param callback the callback
     * @param userName the user name
     * @param email    the email
     * @param password the password
     * @param country  the country
     * @param province the province
     * @param city     the city
     */
    public void register(ResultStatusCallback<User> callback, String userName, String email, String password, String country, String province, String city) {
        registerCommand.execute((result, status) -> {
            loggedInUser = result;
            callback.call(result, status);
        }, userName, email, password, PermissionGroup.REGULAR.name(), country, province, city);
    }

    /**
     * Sets avatar.
     *
     * @param avatar the avatar
     */
    public void setAvatar(Avatar avatar) {
        if (getLoggedInUser() == null) return;
        getLoggedInUser().setAvatar(avatar);
    }
}
