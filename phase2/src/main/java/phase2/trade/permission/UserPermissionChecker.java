package phase2.trade.permission;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.user.User;

/**
 * The User permission checker.
 *
 * @author Dan Lyu
 */
public class UserPermissionChecker {

    private static final Logger logger = LogManager.getLogger(UserPermissionChecker.class);

    private final User operator;

    private final PermissionSet permissionSet;

    private final PermissionGroup permissionGroup;

    /**
     * Constructs a new User permission checker.
     *
     * @param operator      the operator
     * @param permissionSet the permission set
     */
    public UserPermissionChecker(User operator, PermissionSet permissionSet) {
        this.operator = operator;
        this.permissionSet = permissionSet;
        this.permissionGroup = PermissionGroup.UNDEFINED;
    }

    /**
     * Constructs a new User permission checker.
     *
     * @param operator        the operator
     * @param permissionSet   the permission set
     * @param permissionGroup the permission group
     */
    public UserPermissionChecker(User operator, Permission[] permissionSet, PermissionGroup permissionGroup) {
        this.operator = operator;
        this.permissionSet = new PermissionSet(permissionSet);
        this.permissionGroup = permissionGroup;
    }

    /**
     * Check permission boolean.
     *
     * @return the boolean
     */
// 1. If PermissionGroup exists, then only check PermissionGroup
    // 2. If not, check all permissions in PermissionSet
    public boolean checkPermission() {
        if (!permissionGroup.equals(PermissionGroup.UNDEFINED)) {
            return operator.getPermissionGroup().equals(permissionGroup);
        }
        for (Permission permissionRequired : permissionSet.getPerm()) {
            if (!operator.hasPermission(permissionRequired)) {
                logger.debug("[Permission Denied] User: " + operator.getName() + " | " + operator.getPermissionGroup() + " | Required:" + permissionSet + " -> Got" + operator.getPermissionSet());
                return false;
            }
        }
        logger.debug("[Permission Verified] User: " + operator.getName() + " | " + operator.getPermissionGroup() + " | Required:" + permissionSet + " -> Got" + operator.getPermissionSet());
        return true;
    }
}
