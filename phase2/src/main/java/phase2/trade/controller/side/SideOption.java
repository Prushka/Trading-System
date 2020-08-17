package phase2.trade.controller.side;

import phase2.trade.permission.PermissionGroup;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public enum SideOption {

    USER("side.user", "/svg/user.svg", PermissionGroup.REGULAR),
    INVENTORY("side.inventory", "/image/shelf.png", PermissionGroup.REGULAR),
    MARKET("side.market", "/image/marketplace.png", PermissionGroup.REGULAR, PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN, PermissionGroup.GUEST),
    CART("side.cart", "/image/cart.png", PermissionGroup.REGULAR),
    MANAGE_USERS("side.m.users", "/image/process.png", PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),
    MANAGE_USERS_OPERATIONS("side.m.user.ops", "/image/user.png", PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),
    MANAGE_ITEMS("side.m.items", "/image/shelf.png", PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),
    SUPPORT("side.support", "/image/support.png", PermissionGroup.REGULAR, PermissionGroup.FROZEN),
    MANAGE_SUPPORT("side.m.support", "/image/support.png", PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),
    ORDER("side.orders", "/image/team.png", PermissionGroup.REGULAR);


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
