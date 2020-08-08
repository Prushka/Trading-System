package phase2.trade.user;

/*import main.java.com.phase2.trade.item.*;
import main.java.com.phase2.trade.repository.*;
import main.java.com.phase2.trade.repository.reflection.*;*/
import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.database.UserDAO;
import phase2.trade.inventory.Cart;
import phase2.trade.inventory.Inventory;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.ItemManager;


public class PersonalUserManager{

    private PersonalUser loggedInUser;

    private GatewayBundle gatewayBundle;

    private ItemManager itemManager;

    public PersonalUserManager(PersonalUser loggedInUser, GatewayBundle gatewayBundle) {
        this.loggedInUser = loggedInUser;
        this.itemManager = new ItemManager(gatewayBundle, loggedInUser);

    }

    public void addToBeReviewedItem(Callback<Item> itemCallback, Category category, String itemName, String description) {
        itemManager.createAndAddItemToInventory(InventoryType.INVENTORY, itemCallback, category, itemName, description);
    }

    public void removeItemFromInventory(Callback<Item> itemCallback, Long itemId) {
        itemManager.removeItemFrom(InventoryType.INVENTORY, itemCallback, itemId);
    }

    public void addItemToWishlist(Callback<Item> itemCallback, Category category, String itemName, String description) {
        itemManager.createAndAddItemTo(InventoryType.CART, itemCallback, category, itemName, description);
    }

    public void removeItemFromWishlist(Callback<Item> itemCallback, Long itemId) {
        itemManager.removeItemFrom(InventoryType.CART, itemCallback, itemId);
    }

    public void UnfreezeRequest(PersonalUser user) {
        user.setAccountState(AccountState.REQUEST_UNFROZEN);
    }

    public Inventory getUserInventory(PersonalUser user) {
        return user.getInventory();
    }

    public Cart getUserWishlist(PersonalUser user) {
        return user.getCart();
    }

    public List<PersonalUser> suggest(){
        List<Tuple> ans = new ArrayList<>();
        List<User> pu = new ArrayList<>();
        List<User> allUser = userDAO.findAllUser();
        for (User user : allUser){
            if (user instanceof PersonalUser){
                pu.add((PersonalUser) user);
            }
        }
        for (Item i : loggedInUser.getWishList()) {
             for (PersonalUser p : pu) {
                if (p.getInventory().contains(i)){
                    ans.add(new tuple(i,p))
                }
            }
        return ans;
    }

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


