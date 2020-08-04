package src.main.java.com.phase2.trade.user;


import com.phase2.trade.user.AdministrativeUser;
import group.trade.Trade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdministrativeManager {

    private Repository<AdministrativeUser> administrators;
    private Repository<PersonalUser> personalUserRepository;
    private Repository<Trade> tradeRepository;
    private int transactionLimit = 100; //what is the init limit?
    private int lendBeforeBorrow = 1;
    private AdministrativeUser currAdmin = null;
    private PersonalUser currPersonalUser;
    private Iterator<PersonalUser> needToFreezelist;
    private Iterator<PersonalUser> needToConfirmAddItem;
    private Iterator<PersonalUser> needToConfirmUnfreeze;
    private Repository<Item> itemRepository;


    /**
     * all administrative user activities happened here
     * @param administrators A repository of admin in the system
     * @param personalUserRepository repository of all users in the system
     */
    public AdministrativeManager(Repository<AdministrativeUser> administrators, Repository<PersonalUser> personalUserRepository){
        this.administrators = administrators;
        this.personalUserRepository = personalUserRepository;
        needToFreezelist = personalUserRepository.iterator(PersonalUser::getShouldBeFreezedUser);
        needToConfirmAddItem = personalUserRepository.iterator(PersonalUser::getAddToInventoryRequestIsNotEmpty);
        needToConfirmUnfreeze = personalUserRepository.iterator(PersonalUser::getRequestToUnfreeze);
    }

    /**
     * Creates an administrative user, only Head administrative user can create.
     * @param username The username of this c
     * @param email The email of this administrative user
     * @param telephone The telephone of this administrative user
     * @param password The password of this administrative user
     * @param isHead  The boolean if this administrative user is head
     * @return Boolean if this administrative user is successfully created
     */
    public boolean createAdministrator(String username, String email, String telephone, String password, boolean isHead){
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
    public boolean verifyLogin(String username, String password){
        if (getCurrAdmin(username, password) != null){
            return true; //"success.login.adminUser"
        }
        return false; //"failed.login.adminUser"
    }


    /**
     * helper method to check if the administrative user gaven username and password exists
     * @param username This administrative user's username
     * @param password This administrative user's password
     * @return True if exists, otherwise false
     */
    private boolean adminUserExist(String username, String password){
        if (administrators.ifExists(
                AdministrativeUser -> AdministrativeUser.getUserName().equals(username)
                        && AdministrativeUser.getPassword().equals(password))){
            return true;
        }else{
            return false;
        }
    }

    /**
     * helper method, checks if the administrative user exist with the given username
     * @param username The username of the administrative method
     * @return true if exists, otherwise false
     */

    private boolean adminUserExist(String username){
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
    public boolean addSubAdmin(AdministrativeUser head, String username, String email, String telephone, String password){
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
    }

    public Iterator<PersonalUser> getUserRequestToUnfreeze() {
        return needToConfirmUnfreeze;
    }

    /**
     * Freeze a personal user
     * @param user The personal user that is going to be frozen
     */
    public void freezeUser(PersonalUser user){
        user.setIsFrozen(true);
        freezeActivities.add(user);
    }

    /**
     * Unfreeze a personal user
     * @param user The personal user that is going to be unfrozen
     */
    public void unfreezeUser(PersonalUser user){
        user.setIsFrozen(false);
        user.setRequestToUnfreeze(false);
        unfreezeActivities.add(user);
    }

    /**
     * Remove an item from a personal user inventory
     * @param user The personal user
     * @param item The id of the item
     * @return true if the item is removed form the user's inventory, otherwise false
     */
    public boolean removeUserInventoryItem(PersonalUser user, Integer item){
        user.removeFromInventory(item);
        Item itemEntity = itemRepository.get(item);
        itemRepository.remove(itemEntity);
        // itemManager.removeAvailable(itemEntity); need to remove from manager available list
        return true; //"success.remove.item")
    }

    /**
     * Undo the personal user add items to wishlist activity
     * @param user The user that is going to undo
     * @param item Thw item that need to br removed
     */
    public void removeUserWishlistItem(PersonalUser user, Integer item){
        user.removeFromWishList(item);
    }

    /**
     * Confirm to add an item to a personal user's inventory
     * @param user The personal user need to confirm add
     * @param item The item id
     * @return true if successfully added, otherwise false
     */
    public boolean confirmAddItem(PersonalUser user, Integer item) {
        user.addToInventory(item);
        user.removeAddToInventoryRequest(item);
        // Item itemEntity = itemRepository.get(item);
        // itemRepository.add(itemEntity);
        return true; //"success.confirm.AddItem";
    }


    /**
     * Confirm to add all items to a personal user's inventory
     * @param user The personal user
     * @return true if successfully added all items, otherwise false
     */
    public boolean confirmAddAllItemForAUser(PersonalUser user) {
        for (Integer item : user.getAddToInventoryRequest()) {
            user.addToInventory(item);
            user.getAddToInventoryRequest().remove(item);
            Item itemEntity = itemRepository.get(item);
            itemRepository.add(itemEntity);
        }
        return true; //translatable("success.confirm.AddItem").build();
    }

    /**
     * Confirm to add all items request to all personal user's inventory
     * @return true if successfully added all items, otherwise false
     */
    public boolean confirmAddAllItem(){
        while (needToConfirmAddItem.hasNext()){
            confirmAddAllItemRequestForAUser(needToConfirmAddItem.next());
        }
        return true; //"success.confirm.allAddItem"
    }

    public boolean confirmUnfreezeUser(PersonalUser user){
        unfreezeUser(user);
        return true; //"success.confirm.unfreeze" //TODO the method id repeated, delete?
    }

    public boolean confirmUnfreezeAllUser(){
        while (needToConfirmUnfreeze.hasNext()){
            unfreezeUser(needToConfirmUnfreeze.next());
        }
        return true; //"success.confirm.unfreezeAll"
    }

    public boolean confirmFreezeUser(PersonalUser user) {
        freezeUser(user);
        return true; //"success.confirm.freeze")
    }

    public boolean confirmFreezeAllUser(){
        while (needToFreezelist.hasNext()){
            currPersonalUser = needToFreezelist.next();
            freezeUser(currPersonalUser);
        }
        return true; //("success.confirm.freezeAll
    }

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

    public PersonalUser findUser(String username) {
            return personalUserRepository.getFirst(
                    PersonalUser -> PersonalUser.getUserName().equals(username));
    }

    public boolean findUserForAdmin (String username) {
        if (personalUserRepository.ifExists(
                PersonalUser -> PersonalUser.getUserName().equals(username))) {
            PersonalUser foundUser = personalUserRepository.getFirst(
                    PersonalUser -> PersonalUser.getUserName().equals(username));
            return true; //).translatable("success.find.admin", foundUser).build();
        }
        return false; //).translatable("failed.find.user").build();
    }

    public boolean findAdminUser(String username) {
        if (administrators.ifExists(
                AdministrativeUser -> AdministrativeUser.getUserName().equals(username))) {
            AdministrativeUser foundAdmin = administrators.getFirst(
                    AdministrativeUser -> AdministrativeUser.getUserName().equals(username));
            return true; //).translatable("success.find.admin", foundAdmin).build();
        }
        return false; //).translatable("failed.find.admin").build();
    }

    public void incompleteTransactions(){
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
