package phase2.trade.permission;

/**
 * The enum Permission.
 *
 * @author Dan Lyu
 * @author Grace Leung
 */
// Using ALL UPPERCASE letters in enum worsen the readability
// according to the naming convention we should name it that way (CHANGE_THRESHOLD, MANAGE_ITEMS etc.)
// So maybe change them back later
public enum Permission {

    /**
     * Change threshold permission.
     */
    ChangeThreshold,
    /**
     * Trade permission.
     */
    Trade,
    /**
     * Edit trade permission.
     */
    EditTrade,
    /**
     * Confirm trade permission.
     */
    ConfirmTrade,
    /**
     * Manage all items permission.
     */
    ManageAllItems,

    /**
     * Review item permission.
     */
    ReviewItem,

    /**
     * Browse market permission.
     */
    BrowseMarket,

    /**
     * Manage personal items permission.
     */
    ManagePersonalItems,
    /**
     * Manage personal account permission.
     */
    ManagePersonalAccount,
    /**
     * Manage wish list permission.
     */
    ManageWishList,

    /**
     * Manage personal support tickets permission.
     */
    ManagePersonalSupportTickets,
    /**
     * Manage all support tickets permission.
     */
    ManageAllSupportTickets,

    /**
     * Manage users permission.
     */
    ManageUsers,
    /**
     * Manage user operations permission.
     */
    ManageUserOperations,


    /**
     * Create user permission.
     */
    CreateUser
}
