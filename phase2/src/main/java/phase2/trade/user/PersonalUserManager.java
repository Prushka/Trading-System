package phase2.trade.user;

/*import main.java.com.phase2.trade.item.*;
import main.java.com.phase2.trade.repository.*;
import main.java.com.phase2.trade.repository.reflection.*;*/
import phase2.trade.database.Callback;
import phase2.trade.database.ItemDAO;
import phase2.trade.database.PersonalUserDAO;
import phase2.trade.database.UserDAO;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.ItemManager;

import java.util.*;

public class PersonalUserManager {

    private final PersonalUserDAO personalUserDAO;
    private PersonalUser currPersonalUser;
    private ItemManager itemManager;
    private PersonalUser loggedInUser;

    public PersonalUserManager(PersonalUserDAO personalUserdao, ItemManager itemManager) {
        this.personalUserDAO = personalUserdao;
        this.itemManager = itemManager;

    }

    /*public void register(Callback<PersonalUser> callback, String username, String email, String password, String country, String city) {
        personalUserDAO.submitSessionWithTransactionAsync(() -> {
            List<User> usersByEmail = personalUserDAO.findByEmail(email);
            List<User> usersByName = personalUserDAO.findByEmail(email);
            if (usersByEmail.size() == 0 && usersByName.size() == 0) {
                User user = new PersonalUser(username, email, password, country, city);
                personalUserDAO.add(user);
                loggedInUser = new PersonalUser(user);
                callback.call(user);
            }
            callback.call(null);
        });
    }

    public void login(Callback<PersonalUser> callback, String usernameOrEmail, String password) {
        personalUserDAO.submitSessionAsync(() -> {
            List<User> matchedUsers = personalUserDAO.findMatches(usernameOrEmail, password);
            if (matchedUsers.size() > 0) {
                loggedInUser = matchedUsers.get(0);
                callback.call(loggedInUser);
            } else {
                callback.call(null);
            }
        });
    }

    public void logOut(){
        loggedInUser = null;
    }

    public boolean isUserLoggedIn(){
        return loggedInUser!=null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }*/

    public void addToBeReviewedItem(Callback<PersonalUser> userCallback, Callback<Item> itemCallback, Category category, String itemName, String description) {
        itemManager.createAndAddItemTo(InventoryType.INVENTORY, itemCallback, category, itemName, description);
    }

    public void removeItemFromInventory(Callback<Item> itemCallback, PersonalUser user, Long itemId) {
        itemManager.removeItemFrom(InventoryType.INVENTORY, itemCallback, itemId);
    }

    public void addItemToWishlist(Callback<PersonalUser> userCallback, Callback<Item> itemCallback, Category category, String itemName, String description) {
        itemManager.createAndAddItemTo(InventoryType.CART, itemCallback, category, itemName, description);
    }

    public void removeItemFromWishlist(Callback<Item> itemCallback, Long itemId) {
        itemManager.removeItemFrom(InventoryType.CART, itemCallback, itemId);
    }

    public void UnfreezeRequest(PersonalUser user) {
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


