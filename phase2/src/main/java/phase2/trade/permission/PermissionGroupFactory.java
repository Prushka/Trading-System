package phase2.trade.permission;

import phase2.trade.config.PermissionConfig;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;

import java.util.HashSet;

public class PermissionGroupFactory {

    private PermissionConfig permissionConfig;

    public PermissionGroupFactory(PermissionConfig permissionConfig){
        this.permissionConfig = permissionConfig;
    }

    public PermissionSet getUserPermission(PermissionGroup type) { // well, it's directly mapped.
        return permissionConfig.getDefaultPermissions().get(type);
    }
}
