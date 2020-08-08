package phase2.trade.command;

import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.User;

public class UserPermissionChecker {

    private final User operator;

    private final PermissionSet permissionSet;

    public UserPermissionChecker(User operator, PermissionSet permissionSet){
        this.operator = operator;
        this.permissionSet = permissionSet;
    }

    public boolean checkPermission() {
        boolean all = true;
        for (Permission permissionRequired : permissionSet.getPerm()) {
            all = all && operator.hasPermission(permissionRequired);
        }
        return all;
    }
}
