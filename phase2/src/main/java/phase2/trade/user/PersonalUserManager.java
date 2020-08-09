package phase2.trade.user;


import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.database.UserDAO;
import phase2.trade.inventory.Cart;
import phase2.trade.inventory.Inventory;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.command.AddToCart;
import phase2.trade.item.command.ItemCommand;
import phase2.trade.user.command.UserCommand;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;


/*public class PersonalUserManager extends UserCommand<User>{

    private RegularUser loggedInUser;

    private ItemCommand itemCommand;

    private EntityBundle entityBundle;

    /*public PersonalUserManager(RegularUser loggedInUser, EntityBundle entityBundle, ItemCommand itemCommand) {
        super((GatewayBundle) entityBundle);
    }



    /*public void addToBeReviewedItem(Callback<Item> itemCallback, Category category, String itemName, String description) {
        itemManager.createAndAddItemTo(InventoryType.INVENTORY, itemCallback, category, itemName, description);
    }

    public void removeItemFromInventory(Callback<Item> itemCallback, Long itemId) {
        itemManager.removeItemFrom(InventoryType.INVENTORY, itemCallback, itemId);
    }

    public void addItemToWishlist(EntityBundle entityBundle, Long itemID) {
        new AddToCart(entityBundle, loggedInUser, itemID).execute();

    }

    public void removeItemFromWishlist(Callback<Item> itemCallback, Long itemId) {
        itemManager.removeItemFrom(InventoryType.CART, itemCallback, itemId);
    }

    public void UnfreezeRequest(RegularUser user) {
        user.setAccountState(AccountState.REQUEST_UNFROZEN);
    }

    public Inventory getUserInventory(RegularUser user) {
        return user.getInventory();
    }

    public Cart getUserWishlist(RegularUser user) {
        return user.getCart();
    }

    /*public List<RegularUser> suggest(){
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
//}


