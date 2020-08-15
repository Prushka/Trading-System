package phase2.trade.permission;

// Using ALL UPPERCASE letters in enum worsen the readability
// according to the naming convention we should name it that way (CHANGE_THRESHOLD, MANAGE_ITEMS etc.)
// So maybe change them back later
public enum Permission {

    ChangeThreshold,
    Trade,
    EditTrade,
    ConfirmTrade,
    ManageAllItems,

    ReviewItem,

    BrowseMarket,

    ManagePersonalItems,
    ManagePersonalAccount,
    ManageWishList,

    ManagePersonalSupportTickets,
    ManageAllSupportTickets,

    ManageUsers,
    ManageUserOperations,


    CreateUser;
}
