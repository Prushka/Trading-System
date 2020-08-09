package phase2.trade.config;

import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionSet;

import java.util.*;

public class PermissionConfig {

    private Map<PermissionGroup, PermissionSet> defaultPermissions;

    public PermissionConfig() {
        defaultPermissions = new HashMap<>();
        defaultPermissions.put(PermissionGroup.REGULAR, new PermissionSet(Permission.ADD_ITEM, Permission.REMOVE_ITEM));
        defaultPermissions.put(PermissionGroup.ADMIN, new PermissionSet(Permission.CHANGE_THRESHOLD));
        defaultPermissions.put(PermissionGroup.GUEST, new PermissionSet(Permission.REMOVE_ITEM));
    }

    public Map<PermissionGroup, PermissionSet> getDefaultPermissions() {
        return defaultPermissions;
    }

    public void setDefaultPermissions(Map<PermissionGroup, PermissionSet> defaultPermissions) {
        this.defaultPermissions = defaultPermissions;
    }
}