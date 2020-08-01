package com.phase2.trade.user;


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
    private List<PersonalUser> freezeActivities;
    private List<PersonalUser> unfreezeActivities;
    private List<PersonalUser> addItemActivities;


    public AdministrativeManager(){
        needToFreezelist = personalUserRepository.iterator(PersonalUser::getShouldBeFreezedUser);
        needToConfirmAddItem = personalUserRepository.iterator(PersonalUser::getAddToInventoryRequestIsNotEmpty);
        needToConfirmUnfreeze = personalUserRepository.iterator(PersonalUser::getRequestToUnfreeze);
        freezeActivities = new ArrayList();
        unfreezeActivities = new ArrayList();
        addItemActivities = new ArrayList();
    }

    public boolean createAdministrator(String username, String email, String telephone, String password, boolean isHead){
        if (personalUserRepository.ifExists(
                PersonalUser -> PersonalUser.getUserName().equals(username))){
            return false; //username existed
            //return new Response.Builder(false).translatable("existed.username").build();
        }
        if (isHead){
        AdministrativeUser admin = new AdministrativeUser(username, email, telephone, password, true);
        administrators.add(admin);
        return true;
        //return new Response.Builder(true).translatable("success.create.newAdmin").build();
        }else{
            return false; // not head can't register
            //return new Response.Builder(false).translatable("not.head").build();
        }
    }

    public boolean verifyLogin(String username, String password){
         if (getCurrAdmin(username, password) != null){
             return true; //"success.login.adminUser"
         }
         return false; //"failed.login.adminUser"
    }

    public boolean addSubAdmin(AdministrativeUser head, String username, String email, String telephone, String password){
        if (head.getIsHead()){
            createAdministrator(username, email, telephone, password, false);
            return true; //"success.add.subadmin"
        } else{
            return false; //"failed.add.subadmin
        }
    }

    public AdministrativeUser getCurrAdmin(String username, String password){
        if (administrators.ifExists(
                AdministrativeUser -> AdministrativeUser.getUserName().equals(username)
                        && AdministrativeUser.getPassword().equals(password))) {
            currAdmin = administrators.getFirst(
                    AdministrativeUser -> AdministrativeUser.getUserName().equals(username)
                            && AdministrativeUser.getPassword().equals(password));
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

    public void freezeUser(PersonalUser user){
        user.setIsFrozen(true);
        freezeActivities.add(user);
    }

    public void unfreezeUser(PersonalUser user){
        user.setIsFrozen(false);
        user.setRequestToUnfreeze(false);
        unfreezeActivities.add(user);
    }

    public boolean removeUserItem(PersonalUser user, Integer item){
        user.removeFromInventory(item);
        Item itemEntity = itemRepository.get(item);
        itemRepository.remove(itemEntity);
        // itemManager.removeAvailable(itemEntity); need to remove from manager available list
        return true; //"success.remove.item")
    }


    public boolean confirmAddAllItemRequestForAUser(PersonalUser user) {
        for (Integer item : user.getAddToInventoryRequest()) {
            user.addToInventory(item);
            user.getAddToInventoryRequest().remove(item);
            Item itemEntity = itemRepository.get(item);
            itemRepository.add(itemEntity);
        }
        return true; //translatable("success.confirm.AddItem").build();
    }

    public boolean confirmAddItemRequest(PersonalUser user, Integer item) {
        user.addToInventory(item);
        user.removeAddToInventoryRequest(item);
        // Item itemEntity = itemRepository.get(item);
        // itemRepository.add(itemEntity);
        return true; //"success.confirm.AddItem";
    }

    public boolean confirmAddAllItemRequest(){
        while (needToConfirmAddItem.hasNext()){
            confirmAddAllItemRequestForAUser(needToConfirmAddItem.next());
        }
        return true; //"success.confirm.allAddItem"
    }

    public boolean confirmUnfreezeUser(PersonalUser user){
        unfreezeUser(user);
        return true; //"success.confirm.unfreeze"
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




}
