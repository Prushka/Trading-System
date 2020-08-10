package phase2.trade.user;


import phase2.trade.gateway.database.TradeDAO;

import phase2.trade.callback.Callback;
import phase2.trade.gateway.database.UserDAO;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.permission.PermissionGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All activities of an administrative user happens here
 */
public class AdministrativeUserManager {

    private UserDAO userDAO;
    private TradeDAO tradeDAO;
    private List<RegularUser> regularUser = new ArrayList<>();
    private int transactionLimit = 100; //what is the init limit?
    private int lendBeforeBorrow = 1;
    private List<RegularUser> needToFreezeUserList;
    private Map<RegularUser, List<Item>> needToConfirmAddItem;
    private List<RegularUser> needToConfirmUnfreeze;
    private AdministrativeUser loggedInAdminUser;
    //private Repository<Item> itemRepository;


    /**
     * all administrative user activities happened here
     *
     * @param userDAO A DAO of all users in the system
     */
    public void AdministrativeManager(UserDAO userDAO, TradeDAO tradeDAO, AdministrativeUser loggedInAdminUser) {
        this.userDAO = userDAO;
        this.tradeDAO = tradeDAO;
        needToFreezeUserList = new ArrayList<>();
        needToConfirmAddItem = new HashMap<>();
        needToConfirmUnfreeze = new ArrayList<>();
        this.loggedInAdminUser = loggedInAdminUser;
    }

    public List<RegularUser> findAllPersonalUser() {
        List<User> allUser = userDAO.findAllUser();
        for (User user : allUser) {
            if (user instanceof RegularUser) {
                regularUser.add((RegularUser) user);
            }
        }
        return regularUser;
    }

    public List<RegularUser> getPersonalUserNeedToFreeze() {
        for (RegularUser user : regularUser) {
            if (tradeDAO.findNumOfLending(user) - lendBeforeBorrow < tradeDAO.findNumOfBorrowing(user)) {
                needToFreezeUserList.add(user);
            }
        }
        return needToFreezeUserList;
    }

    public List<RegularUser> getNeedToConfirmUnfreeze() {
        for (RegularUser user : regularUser) {
            if (user.getAccountState() == AccountState.REQUEST_UNFROZEN) {
                needToConfirmUnfreeze.add(user);
            }
        }
        return needToConfirmUnfreeze;
    }

    public Map<RegularUser, List<Item>> getNeedToConfirmAddItem() {
        for (RegularUser user : regularUser) {
            List<Item> items = user.getInventory().getListOfItems();
            List<Item> reviewList = new ArrayList<>();
            for (Item item : items) {
                if (item.getOwnership() == Ownership.TO_BE_REVIEWED) {
                    reviewList.add(item);
                }
            }
            if (!reviewList.isEmpty()) {
                needToConfirmAddItem.put(user, reviewList);
            }
        }
        return needToConfirmAddItem;
    }


    /**
     * Freeze a personal user
     *
     * @param user The personal user that is going to be frozen
     */
    public void freezeUser(RegularUser user) {
        user.setAccountState(AccountState.FROZEN);
    }

    public void FreezeAllUser() {
        for (RegularUser user : getPersonalUserNeedToFreeze()) {
            freezeUser(user);
        }
    }

    /**
     * Unfreeze a personal user
     *
     * @param user The personal user that is going to be unfrozen
     */
    public void unfreezeUser(RegularUser user) {
        user.setAccountState(AccountState.NORMAL);
    }

    public void UnfreezeAllUser() {
        for (RegularUser user : getNeedToConfirmUnfreeze()) {
            unfreezeUser(user);
        }
    }

    /**
     * Remove an item from a personal user inventory
     * @param user   The personal user
     * @param itemId The id of the item
     * @return true if the item is removed form the user's inventory, otherwise false
     */
    public void removeItemFromPersonalInventory(RegularUser user, Long itemId) {
        if (user.getInventory().findByUid(itemId) != null){
            user.getInventory().removeItemByUid(itemId);
        }
    }

    /**
     * Undo the personal user add items to wishlist activity
     * //* @param user The user that is going to undo
     *
     * @param itemId Thw item that need to br removed
     */
    public void removeItemFromWishlist(RegularUser user, Long itemId) {
        if (user.getCart().findByUid(itemId) != null){
            user.getCart().removeItemByUid(itemId);
        }
    }

    /**
     * Confirm to add an item to a personal user's inventory
     * @param user The personal user need to confirm add
     * @param item The item
     */
    public void confirmAddItemToInventory(RegularUser user, Item item) {
        List<Item> reviewItem = needToConfirmAddItem.get(user);
        item.setOwner(user);
        item.setOwnership(Ownership.OWNER);
        reviewItem.remove(item);
    }


    /**
     * Confirm to add all items requested to a personal user's inventory
     * @param user The personal user
     */
    public void confirmAddAllItemForAUser(RegularUser user) {
        List<Item> reviewItem = needToConfirmAddItem.get(user);
        for (Item item : reviewItem) {
            item.setOwner(user);
            item.setOwnership(Ownership.OWNER);
            reviewItem.remove(item);
        }

    }

    public int getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(int limit) {
        transactionLimit = limit;
    }

    public int getLendBeforeBorrowLimit() {
        return lendBeforeBorrow;
    }

    public void setLendBeforeBorrowLimit(int limit) {
        lendBeforeBorrow = limit;

    }/**
     * Creates an administrative user, only Head administrative user can create.
     *
     * @param username The username of this c
     * @param email    The email of this administrative user
     * @param password The password of this administrative user
     * @return Boolean if this administrative user is successfully created
     */
    public void createAdministrator(String username, String email, String password, String country, String city) {
        AdministrativeUser admin = new AdministrativeUser(username, email, password, country, city);
        userDAO.add(admin);
    }

    /**
     * Head administrative user creates a subsequent administrative user, only head administrative user can create
     * subsequent  administrative user
     *
     * @param username The username of the subsequent  administrative user
     * @param email    The email of the subsequent administrative user
     * @param password The password of the subsequent administrative user
     * @return Boolean if the subsequent administrative user is successfully created
     */
    public boolean addSubAdmin(String username, String email, String password, String country, String city) {
        if (loggedInAdminUser.getPermissionGroup() == PermissionGroup.HEADADMIN) {
            createAdministrator(username, email, password, country, city);
            return true;
        } else {
            return false; //not a head admin
        }
    }

    /*
    public void cancelTrade(Trade trade){
    }*///TODO ask to combine the cancel trade method from trade manager


}


    /**
     * helper method, checks if the administrative user exist with the given username
     * @param username The username of the administrative method
     * @return true if exists, otherwise false
     */

    /*private boolean adminUserExist(String username){
        if (administrators.ifExists(
                AdministrativeUser -> AdministrativeUser.getUserName().equals(username))){
            return true; //username existed
    }else{
            return false;
        }
    }

     /*public void incompleteTransactions(){
        int incomplete = 0;
        List<Integer> allTrades = currPersonalUser.getTrades();
        for(Integer i: allTrades){
            Trade trade = tradeRepository.get(i);
            if (!trade.getIsClosed() && trade.getPrevMeeting() == null){
                incomplete++;
            }
        }
    }
     */

