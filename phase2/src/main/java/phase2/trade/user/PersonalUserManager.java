package phase2.trade.user;

/*import main.java.com.phase2.trade.item.*;
import main.java.com.phase2.trade.repository.*;
import main.java.com.phase2.trade.repository.reflection.*;*/
import phase2.trade.gateway.Callback;
import phase2.trade.gateway.database.UserDAO;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.ItemManager;


public class PersonalUserManager {

    private final UserDAO UserDAO;
    private RegularUser currRegularUser;
    private ItemManager itemManager;
    private RegularUser loggedInUser;

    public PersonalUserManager(UserDAO UserDAO, ItemManager itemManager) {
        this.UserDAO = UserDAO;
        this.itemManager = itemManager;

    }

    /*
    public void addToBeReviewedItem(Callback<RegularUser> userCallback, Callback<Item> itemCallback, Category category, String itemName, String description) {
        itemManager.createAndAddItemTo(InventoryType.INVENTORY, itemCallback, category, itemName, description);
    }

    public void removeItemFromInventory(Callback<Item> itemCallback, RegularUser user, Long itemId) {
        itemManager.removeItemFrom(InventoryType.INVENTORY, itemCallback, itemId);
    }

    public void addItemToWishlist(Callback<RegularUser> userCallback, Callback<Item> itemCallback, Category category, String itemName, String description) {
        itemManager.createAndAddItemTo(InventoryType.CART, itemCallback, category, itemName, description);
    }

    public void removeItemFromWishlist(Callback<Item> itemCallback, Long itemId) {
        itemManager.removeItemFrom(InventoryType.CART, itemCallback, itemId);
    }

    public void UnfreezeRequest(RegularUser user) {
        user.setAccountState(AccountState.REQUEST_UNFROZEN);
    }

//    public List<Integer> getUserInventory(PersonalUser user) {
//        return user.getInventory();
//    }
//
//    public List<Integer> getUserWishlist(PersonalUser user) {
//        return user.getWishlist();
//    }

    /*public void suggest (PersonalUser p) {
        for (PersonalUser x : personalUserRepository) {
            for (Item i : x.getInventory()) {
                if (p.getWishlist().contains(i)) {
                    p.suggest(i);
                }
            }
        }
    }*/
}


