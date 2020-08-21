package phase2.trade.config;

import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionSet;

import java.util.HashMap;
import java.util.Map;

/**
 * The Permission config.
 *
 * @author Dan Lyu
 * @author Grace Leung
 */
public class PermissionConfig {

    private Map<PermissionGroup, PermissionSet> defaultPermissions = new HashMap<>();

    /**
     * Constructs a new Permission config with the default parameters.
     */
    public PermissionConfig() {

        addPermissions(PermissionGroup.GUEST, Permission.BrowseMarket);

        addPermissions(PermissionGroup.REGULAR,
                Permission.ManagePersonalAccount,
                Permission.ManagePersonalItems,
                Permission.ManageWishList,
                Permission.Trade,
                Permission.EditTrade,
                Permission.ConfirmTrade,
                Permission.BrowseMarket,
                Permission.ManagePersonalSupportTickets);

        addPermissions(PermissionGroup.ADMIN,
                Permission.ChangeThreshold,
                Permission.ReviewItem,
                Permission.BrowseMarket);

        addPermissions(PermissionGroup.HEAD_ADMIN,
                Permission.CreateUser,
                Permission.ChangeThreshold,
                Permission.ReviewItem,
                Permission.ManageUserOperations,
                Permission.ManageUsers,
                Permission.ManageAllItems,
                Permission.ManageAllSupportTickets,
                Permission.BrowseMarket);

        addPermissions(PermissionGroup.SYSTEM, Permission.ChangeThreshold, Permission.CreateUser);
    }

    private void addPermissions(PermissionGroup permissionGroup, Permission... permissions) {
        defaultPermissions.put(permissionGroup, new PermissionSet(permissions));
    }

    /**
     * Gets default permissions.
     *
     * @return the default permissions
     */
    public Map<PermissionGroup, PermissionSet> getDefaultPermissions() {
        return defaultPermissions;
    }

    /**
     * Sets default permissions.
     *
     * @param defaultPermissions the default permissions
     */
    public void setDefaultPermissions(Map<PermissionGroup, PermissionSet> defaultPermissions) {
        this.defaultPermissions = defaultPermissions;
    }
}
