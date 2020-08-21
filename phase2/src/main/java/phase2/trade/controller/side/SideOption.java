package phase2.trade.controller.side;

import phase2.trade.config.controller.AdministrativeConfigController;
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

/**
 * The enum Side option.
 *
 * @author Dan Lyu
 */
public enum SideOption {

    /**
     * User side option.
     */
    USER("side.user",
            "/svg/user.svg",
            UserInfoController::new,
            PermissionGroup.REGULAR, PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),

    /**
     * Market side option.
     */
    MARKET("side.market",
            "/svg/marketplace.svg",
            MarketListController::new,
            PermissionGroup.REGULAR, PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN, PermissionGroup.GUEST),

    /**
     * Inventory side option.
     */
    INVENTORY("side.inventory",
            "/svg/stock.svg",
            InventoryController::new,
            PermissionGroup.REGULAR),

    /**
     * Cart side option.
     */
    CART("side.cart",
            "/svg/supermarket.svg",
            CartController::new,
            PermissionGroup.REGULAR),

    /**
     * Order side option.
     */
    ORDER("side.trades",
            "/svg/team.svg",
            TradeListController::new,
            PermissionGroup.REGULAR),

    /**
     * Manage users side option.
     */
    MANAGE_USERS("side.m.users",
            "/svg/process.svg",
            UserManageController::new,
            PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),

    /**
     * Manage users operations side option.
     */
    MANAGE_USERS_OPERATIONS(
            "side.m.user.ops",
            "/svg/users.svg",
            UserOperationController::new,
            PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),

    /**
     * Manage items side option.
     */
    MANAGE_ITEMS("side.m.items",
            "/svg/stock.svg",
            ItemManageController::new,
            PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),

    /**
     * Support side option.
     */
    SUPPORT("side.support",
            "/svg/support.svg",
            SupportTicketUserController::new,
            PermissionGroup.REGULAR, PermissionGroup.FROZEN),

    /**
     * Manage support side option.
     */
    MANAGE_SUPPORT("side.m.support",
            "/svg/support.svg",
            SupportTicketAdminController::new,
            PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),

    /**
     * Config setting side option.
     */
    CONFIG_SETTING("side.config",
            "/svg/settings.svg",
            AdministrativeConfigController::new,
            PermissionGroup.ADMIN, PermissionGroup.HEAD_ADMIN),


    /**
     * Exit side option.
     */
    EXIT(SidePosition.BOTTOM,
            "side.exit",
            "/svg/turnoff.svg"),

    /**
     * Sign out side option.
     */
    SIGN_OUT(SidePosition.BOTTOM,
            "side.sign.out",
            "/svg/logout.svg");


    /**
     * The enum Side position.
     *
     * @author Dan Lyu
     */
    enum SidePosition {
        /**
         * Top side position.
         */
        TOP,

        /**
         * Bottom side position.
         */
        BOTTOM
    }

    /**
     * The Icon path.
     */
    public String iconPath;

    /**
     * The Language.
     */
    public String language;

    private final Collection<PermissionGroup> permissionGroups = new HashSet<>();

    /**
     * The Side position.
     */
    public SidePosition sidePosition;

    /**
     * The Controller supplier.
     */
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

    /**
     * If display this {@link SideOption} to the specific {@link PermissionGroup}.
     *
     * @param permissionGroup the permission group
     * @return the boolean
     */
    public boolean ifDisplay(PermissionGroup permissionGroup) {
        return permissionGroups.contains(permissionGroup) || permissionGroups.size() == 0;
    }
}
