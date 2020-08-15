package phase2.trade.config;

import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionSet;

import java.util.*;

public class PermissionConfig {

    private Map<PermissionGroup, PermissionSet> defaultPermissions = new HashMap<>();

    public PermissionConfig() {

        addPermissions(PermissionGroup.GUEST, Permission.BrowseMarket);

        addPermissions(PermissionGroup.REGULAR,
                Permission.ManagePersonalItems,
                Permission.ManageWishList,
                Permission.Trade,
                Permission.EditTrade,
                Permission.ConfirmTrade,
                Permission.BrowseMarket);

        addPermissions(PermissionGroup.ADMIN, Permission.ChangeThreshold, Permission.ReviewItem);
        addPermissions(PermissionGroup.HEAD_ADMIN, Permission.CreateUser,
                Permission.ChangeThreshold, Permission.ReviewItem, Permission.ManageUserOperations, Permission.ManageUsers,
                Permission.ManageAllItems);

        addPermissions(PermissionGroup.SYSTEM, Permission.ChangeThreshold, Permission.CreateUser);
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
