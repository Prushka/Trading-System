package phase2.trade.permission;

import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.User;

public class UserPermissionChecker {

    private final User operator;

    private final PermissionSet permissionSet;

    private final PermissionGroup permissionGroup;

    public UserPermissionChecker(User operator, PermissionSet permissionSet) {
        this.operator = operator;
        this.permissionSet = permissionSet;
        this.permissionGroup = PermissionGroup.UNDEFINED;
    }

    public UserPermissionChecker(User operator, Permission[] permissionSet, PermissionGroup permissionGroup) {
        this.operator = operator;
        this.permissionSet = new PermissionSet(permissionSet);
        this.permissionGroup = permissionGroup;
    }

    // 1. If PermissionGroup exists, then only check PermissionGroup
    // 2. If not, check all permissions in PermissionSet
    public boolean checkPermission() {
        if (!permissionGroup.equals(PermissionGroup.UNDEFINED)) {
            return operator.getPermissionGroup().equals(permissionGroup);
        }
        for (Permission permissionRequired : permissionSet.getPerm()) {
            if (!operator.hasPermission(permissionRequired)) return false;
        }
        return true;
    }
}
