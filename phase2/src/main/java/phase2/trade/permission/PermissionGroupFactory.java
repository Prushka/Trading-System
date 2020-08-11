package phase2.trade.permission;

import phase2.trade.config.PermissionConfig;

public class PermissionGroupFactory {

    private PermissionConfig permissionConfig;

    public PermissionGroupFactory(PermissionConfig permissionConfig){
        this.permissionConfig = permissionConfig;
    }

    public PermissionSet getUserPermission(PermissionGroup type) { // well, it's directly mapped.
        return permissionConfig.getDefaultPermissions().get(type);
    }
}
