package phase2.trade.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.config.PermissionConfig;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionSet;

/**
 * The User factory.
 *
 * @author Dan Lyu
 */
public class UserFactory {

    private static final Logger logger = LogManager.getLogger(UserFactory.class);

    private final PermissionConfig permissionConfig;

    /**
     * Constructs a new User factory.
     *
     * @param permissionConfig the permission config
     */
    public UserFactory(PermissionConfig permissionConfig) {
        this.permissionConfig = permissionConfig;
    }

    /**
     * Create by permission group user.
     *
     * @param userName              the user name
     * @param email                 the email
     * @param password              the password
     * @param permissionGroupString the permission group string
     * @return the user
     */
    public User createByPermissionGroup(String userName, String email, String password, String permissionGroupString) { // country and city are not necessary for all users
        PermissionGroup permissionGroup = PermissionGroup.valueOf(permissionGroupString);
        User user;
        switch (permissionGroup) {
            case ADMIN:
            case HEAD_ADMIN:
                user = new AdministrativeUser(userName, email, password);
                break;

            case REGULAR:
            case FROZEN:
            case BANNED:
                user = new RegularUser(userName, email, password);
                break;

            case SYSTEM:
                user = new SystemUser();
                break;

            default:
                user = new Guest();
                break;
        }
        user.setPermissionGroup(permissionGroup);
        PermissionSet fromConfig = permissionConfig.getDefaultPermissions().get(permissionGroup);
        if (fromConfig != null) {
            user.setUserPermission(new PermissionSet(fromConfig.getPerm()));
        } else {
            logger.error("Permission Not Set For Group: " + permissionGroup);
        }
        return user;
    }

    /**
     * Configure system user user.
     *
     * @return the user
     */
    public User configureSystemUser() { // system user will not be persistent
        return createByPermissionGroup("SYSTEM", "SYSTEM", "", PermissionGroup.SYSTEM.name());
    }

    /**
     * Configure guest user.
     *
     * @return the user
     */
    public User configureGuest() { // system user will not be persistent
        return createByPermissionGroup("Guest", "guest@example.com", "", PermissionGroup.GUEST.name());
    }
}
