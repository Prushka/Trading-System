package phase2.trade.controller.side;

import phase2.trade.permission.PermissionGroup;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public enum SideOption {

    USER("side.user", "/svg/user.svg", PermissionGroup.REGULAR),
    INVENTORY("side.inventory", "/svg/stock.svg", PermissionGroup.REGULAR),
    MARKET("side.market", "/svg/marketplace.svg", PermissionGroup.REGULAR, PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN, PermissionGroup.GUEST),
    CART("side.cart", "/svg/supermarket.svg", PermissionGroup.REGULAR),
    MANAGE_USERS("side.m.users", "/svg/process.svg", PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),
    MANAGE_USERS_OPERATIONS("side.m.user.ops", "/svg/users.svg", PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),
    MANAGE_ITEMS("side.m.items", "/svg/stock.svg", PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),
    SUPPORT("side.support", "/svg/support.svg", PermissionGroup.REGULAR, PermissionGroup.FROZEN),
    MANAGE_SUPPORT("side.m.support", "/svg/support.svg", PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),
    ORDER("side.orders", "/svg/team.svg", PermissionGroup.REGULAR);

    public String resourcePath;
    public String language;
    private Collection<PermissionGroup> permissionGroups = new HashSet<>();


    SideOption(String language, String resourcePath, PermissionGroup... permissionGroups) {
        this.resourcePath = resourcePath;
        this.language = language;
        this.permissionGroups.addAll(Arrays.asList(permissionGroups));
    }

    SideOption(String language, String resourcePath) {
        this.resourcePath = resourcePath;
        this.language = language;
    }

    public boolean ifDisplay(PermissionGroup permissionGroup) {
        return permissionGroups.contains(permissionGroup) || permissionGroups.size() == 0;
    }
}
