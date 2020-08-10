package phase2.trade.command;

import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.User;

public class UserPermissionChecker {

    private final User operator;

    private final PermissionSet permissionSet;

    public UserPermissionChecker(User operator, PermissionSet permissionSet) {
        this.operator = operator;
        this.permissionSet = permissionSet;
    }

    public UserPermissionChecker(User operator, Permission[] permissionSet) {
        this.operator = operator;
        this.permissionSet = new PermissionSet(permissionSet);
    }

    public boolean checkPermission() {
        for (Permission permissionRequired : permissionSet.getPerm()) {
            if (!operator.hasPermission(permissionRequired)) return false;
        }
        return true;
    }
}
