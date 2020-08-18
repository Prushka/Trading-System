package phase2.trade.controller.side;

import phase2.trade.controller.ControllerSupplier;
import phase2.trade.controller.market.MarketListController;
import phase2.trade.item.controller.CartController;
import phase2.trade.item.controller.InventoryController;
import phase2.trade.item.controller.ItemManageController;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.support.controller.SupportTicketAdminController;
import phase2.trade.support.controller.SupportTicketUserController;
import phase2.trade.trade.controller.TradeListController;
import phase2.trade.user.controller.UserInfoController;
import phase2.trade.user.controller.UserManageController;
import phase2.trade.user.controller.UserOperationController;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public enum SideOption {

    USER("side.user",
            "/svg/user.svg",
            UserInfoController::new,
            PermissionGroup.REGULAR),

    MARKET("side.market",
            "/svg/marketplace.svg",
            MarketListController::new,
            PermissionGroup.REGULAR, PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN, PermissionGroup.GUEST),

    INVENTORY("side.inventory",
            "/svg/stock.svg",
            InventoryController::new,
            PermissionGroup.REGULAR),

    CART("side.cart",
            "/svg/supermarket.svg",
            CartController::new,
            PermissionGroup.REGULAR),

    ORDER("side.trades",
            "/svg/team.svg",
            TradeListController::new,
            PermissionGroup.REGULAR),

    MANAGE_USERS("side.m.users",
            "/svg/process.svg",
            UserManageController::new,
            PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),

    MANAGE_USERS_OPERATIONS(
            "side.m.user.ops",
            "/svg/users.svg",
            UserOperationController::new,
            PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),

    MANAGE_ITEMS("side.m.items",
            "/svg/stock.svg",
            ItemManageController::new,
            PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),

    SUPPORT("side.support",
            "/svg/support.svg",
            SupportTicketUserController::new,
            PermissionGroup.REGULAR, PermissionGroup.FROZEN),

    MANAGE_SUPPORT("side.m.support",
            "/svg/support.svg",
            SupportTicketAdminController::new,
            PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),


    EXIT(SidePosition.BOTTOM,
            "side.exit",
            "/svg/turnoff.svg"),

    SIGN_OUT(SidePosition.BOTTOM,
            "side.sign.out",
            "/svg/logout.svg");


    enum SidePosition {
        TOP, BOTTOM
    }

    public String iconPath;
    public String language;
    private final Collection<PermissionGroup> permissionGroups = new HashSet<>();
    public SidePosition sidePosition;
    public ControllerSupplier<?> controllerSupplier;


    SideOption(SidePosition sidePosition, String language, String iconPath, ControllerSupplier<?> controllerSupplier, PermissionGroup... permissionGroups) {
        this.sidePosition = sidePosition;
        this.iconPath = iconPath;
        this.language = language;
        this.controllerSupplier = controllerSupplier;
        if (permissionGroups.length > 0)
            this.permissionGroups.addAll(Arrays.asList(permissionGroups));
    }

    SideOption(String language, String iconPath, ControllerSupplier<?> controllerSupplier, PermissionGroup... permissionGroups) {
        this(SidePosition.TOP, language, iconPath, controllerSupplier, permissionGroups);
    }

    SideOption(SidePosition sidePosition, String language, String iconPath, PermissionGroup... permissionGroups) {
        this(sidePosition, language, iconPath, null, permissionGroups);
    }

    public boolean ifDisplay(PermissionGroup permissionGroup) {
        return permissionGroups.contains(permissionGroup) || permissionGroups.size() == 0;
    }
}
