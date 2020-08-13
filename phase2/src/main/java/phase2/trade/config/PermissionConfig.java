package phase2.trade.config;

import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionSet;

import java.util.*;

public class PermissionConfig {

    private Map<PermissionGroup, PermissionSet> defaultPermissions = new HashMap<>();

    public PermissionConfig() {

        addPermissions(PermissionGroup.GUEST, Permission.BROWSE_MARKET);

        addPermissions(PermissionGroup.REGULAR,
                Permission.MANAGE_PERSONAL_ITEMS,
                Permission.MANAGE_WISH_LIST,
                Permission.TRADE,
                Permission.EDIT_TRADE,
                Permission.CONFIRM_TRADE,
                Permission.BROWSE_MARKET);

        addPermissions(PermissionGroup.ADMIN, Permission.CHANGE_THRESHOLD, Permission.REVIEW_ITEM);
        addPermissions(PermissionGroup.HEAD_ADMIN, Permission.CHANGE_THRESHOLD, Permission.REVIEW_ITEM, Permission.BROWSE_USER_OPERATIONS);

        addPermissions(PermissionGroup.SYSTEM, Permission.CHANGE_THRESHOLD, Permission.CREATE_USER);
    }

    private void addPermissions(PermissionGroup permissionGroup, Permission... permissions) {
        defaultPermissions.put(permissionGroup, new PermissionSet(permissions));
    }

    public Map<PermissionGroup, PermissionSet> getDefaultPermissions() {
        return defaultPermissions;
    }

    public void setDefaultPermissions(Map<PermissionGroup, PermissionSet> defaultPermissions) {
        this.defaultPermissions = defaultPermissions;
    }
}
