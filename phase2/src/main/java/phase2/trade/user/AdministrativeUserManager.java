package phase2.trade.user;


import phase2.trade.user.AdministrativeUser;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.database.UserDAO;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.ItemManager;
import phase2.trade.item.Ownership;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AdministrativeUserManager {

    private UserDAO userDAO;
    private ItemManager itemManager;
    //private Repository<Trade> tradeRepository;
    private List<PersonalUser> personalUser = new ArrayList<>();
    private int transactionLimit = 100; //what is the init limit?
    private int lendBeforeBorrow = 1;
    private List<PersonalUser> needToFreezeList;
    private List<PersonalUser> needToConfirmAddItem;
    private List<PersonalUser> needToConfirmUnfreeze;
    //private Repository<Item> itemRepository;


    /**
     * all administrative user activities happened here
     * @param userDAO A DAO of all users in the system
     */
    public void AdministrativeManager(UserDAO userDAO, ItemManager itemManager){
        this.userDAO = userDAO;
        this.itemManager = itemManager;
        needToFreezeList = new ArrayList<>();
        needToConfirmAddItem = new ArrayList<>();
        needToConfirmUnfreeze = new ArrayList<>();
    }

    public List<PersonalUser> findAllPersonalUser(){
         List<User> allUser = userDAO.findAllUser();
         for (User user : allUser){
             if (user instanceof PersonalUser){
                 personalUser.add((PersonalUser) user);
             }
         }
         return personalUser;
    }

    /*public List<PersonalUser> getPersonalUserNeedToFreeze(){
        for (PersonalUser user: personalUser){
            if (user.get)
        }
    }*/
    public List<PersonalUser> getNeedToConfirmUnfreeze(){
        for (PersonalUser user: personalUser){
            if (user.getAccountState() == AccountState.REQUEST_UNFROZEN){
                needToConfirmUnfreeze.add(user);
            }
        }
        return needToConfirmUnfreeze;
    }

    /*public List<PersonalUser> getNeedToConfirmAddItem()*/


    /**
     * Creates an administrative user, only Head administrative user can create.
     * @param username The username of this c
     * @param email The email of this administrative user
     * @param telephone The telephone of this administrative user
     * @param password The password of this administrative user
     * @param isHead  The boolean if this administrative user is head
     * @return Boolean if this administrative user is successfully created
     */
   /* public boolean createAdministrator(String username, String email, String telephone, String password, boolean isHead){
        if (adminUserExist(username)){
            return false; //username existed
            //existed.username";
        }
        if (isHead){
        AdministrativeUser admin = new AdministrativeUser(username, email, telephone, password, true);
        administrators.add(admin);
        return true; //"success.create.newAdmin"
        }else{
            return false; // not head can't register, "not.head"
        }
    }

    /**
     * Check if the administrative user gives the correct username and password to login
     * @param username This administrative user's username
     * @param password This administrative user's password
     * @return True if correct, otherwise false
     */



    /**
     * helper method to check if the administrative user gaven username and password exists
     * @param username This administrative user's username
     * @param password This administrative user's password
     * @return True if exists, otherwise false
     */
    /*private boolean adminUserExist(String username, String password){
        if (administrators.ifExists(
                AdministrativeUser -> AdministrativeUser.getUserName().equals(username)
                        && AdministrativeUser.getPassword().equals(password))){
            return true;
        }else{
            return false;
        }
    }*/

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
    /**
     * Head administrative user creates a subsequent administrative user, only head administrative user can create
     * subsequent  administrative user
     * @param head The head administrative user that is creating the subsequent  administrative user
     * @param username The username of the subsequent  administrative user
     * @param email The email of the subsequent administrative user
     * @param telephone The telephone of the subsequent administrative user
     * @param password The password of the subsequent administrative user
     * @return Boolean if the subsequent administrative user is successfully created
     */
    /*public boolean addSubAdmin(AdministrativeUser head, String username, String email, String telephone, String password){
        if (head.getIsHead()){
            createAdministrator(username, email, telephone, password, false);
            return true; //"success.add.subadmin"
        } else{
            return false; //"failed.add.subadmin
        }
    }

    public AdministrativeUser getCurrAdmin(String username, String password){
        if (adminUserExist(username, password)) {
            currAdmin = administrators.getFirst(
                    AdministrativeUser -> AdministrativeUser.getUserName().equals(username)
                            && AdministrativeUser.getPassword().equals(password));
        }else {
            currAdmin = null;
        }
        return currAdmin;
    }

    public PersonalUser getCurrPersonalUser(){
        currPersonalUser = needToFreezelist.next();
        return currPersonalUser;
    }

    public Iterator<PersonalUser> getListUserShouldBeFreezed(){
        return needToFreezelist;
    }

    public String getNeedToConfirmAddItemUserList(){
        StringBuilder stringBuilder = new StringBuilder();
        while (needToConfirmAddItem.hasNext()) {
            PersonalUser user = needToConfirmAddItem.next();
            stringBuilder.append("User ID: ").append(user.getUid()).append(" | Request IDs: ")
                    .append(user.getAddToInventoryRequest()).append("\n");
        }
        return stringBuilder.toString();
    }*/



    /**
     * Freeze a personal user
     * @param user The personal user that is going to be frozen
     */
    public void freezeUser(PersonalUser user){
        user.setAccountState(AccountState.FROZEN);
    }

    /*public void confirmFreezeAllUser(){
        for (PersonalUser user : getPersonalUserNeedToFreeze()){
            freezeUser(user);
        }
    }*/

    /**
     * Unfreeze a personal user
     * @param user The personal user that is going to be unfrozen
     */
    public void unfreezeUser(PersonalUser user){
        user.setAccountState(AccountState.NORMAL);
    }

    public void confirmUnfreezeAllUser(){
       for (PersonalUser user : getNeedToConfirmUnfreeze()){
           unfreezeUser(user);
       }
    }

    /**
     * Remove an item from a personal user inventory
     * @param user The personal user
     * @param itemId The id of the item
     * @return true if the item is removed form the user's inventory, otherwise false
     */
    public void removeItemFromPersonalInventory(Callback<Item> itemCallback, PersonalUser user, Long itemId) {
        itemManager.removeItemFrom(InventoryType.INVENTORY, itemCallback, itemId);
    }


    /**
     * Undo the personal user add items to wishlist activity
     //* @param user The user that is going to undo
     * @param itemId Thw item that need to br removed
     */
    public void removeItemFromWishlist(Callback<Item> itemCallback, Long itemId) {
        itemManager.removeItemFrom(InventoryType.CART, itemCallback, itemId);
    }

    /**
     * Confirm to add an item to a personal user's inventory
     //* @param user The personal user need to confirm add
     * @param itemID The item id
     * @return true if successfully added, otherwise false
     */
    public void confirmAddItemToInventory(Callback<Boolean> callback, Callback<Item> itemCallback, Long itemID) {
        itemManager.reviewItem(callback, Ownership.OWNER, itemID);
    }


    /**
     * Confirm to add all items to a personal user's inventory
     //* @param user The personal user
     * @return true if successfully added all items, otherwise false
     */
    /*public boolean confirmAddAllItemForAUser(PersonalUser user) {
        new ItemManager(,user)
        }
        return true; //translatable("success.confirm.AddItem").build();
    }*/


    public int getTransactionLimit(){
        return transactionLimit;
    }

    public void setTransactionLimit(int limit){
        transactionLimit = limit;
    }

    public int getLendBeforeBorrowLimit(){
        return lendBeforeBorrow;
    }

    public void setLendBeforeBorrowLimit(int limit){
        lendBeforeBorrow = limit;
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



    /*
    public void cancelTrade(Trade trade){
    }*///TODO ask to combine the cancel trade method from trade manager




}
