package group.user;

import group.item.Item;
import group.menu.data.Response;
import group.repository.Repository;

import java.util.*;

public class AdministrativeManager {


    private Repository<AdministrativeUser> administrators;
    private Repository<PersonalUser> personalUserRepository;
    private Iterator<PersonalUser> needToFreezelist;
    private int transactionLimit = 100; //what is the init limit?
    private int lendBeforeBorrow = 1;
    private AdministrativeUser currAdmin;
    private PersonalUser currPersonalUser;
    private Iterator<PersonalUser> needToConfirmAddItem;
    private Iterator<PersonalUser> userRequestToUnfreeze;


    public AdministrativeManager(Repository<AdministrativeUser> administrativeUserRepository,
                                 Repository<PersonalUser> personalUserRepository){
        this.administrators = administrativeUserRepository;
        this.personalUserRepository = personalUserRepository;
        needToFreezelist = personalUserRepository.iterator(PersonalUser::getShouldBeFreezedUser);
        needToConfirmAddItem = personalUserRepository.iterator(PersonalUser::getAddToInventoryRequestIsNotEmpty);
        userRequestToUnfreeze = personalUserRepository.iterator(PersonalUser::getRequestToUnfreeze);
    }

    public Response createAdministrator(String username, String email, String telephone, String password, boolean isHead){
        AdministrativeUser admin = new AdministrativeUser(username, email, telephone, password, isHead);
        administrators.add(admin);
        return new Response.Builder(true).translatable("success.create.new").build();
    }

    public Response verifyLogin(String username, String password){
         if (getCurrAdmin(username, password) != null){
             return new Response.Builder(true).translatable("success.login.user").build();
         }
         return new Response.Builder(false).translatable("failed.login.user").build();
    }

    public Response addSubAdmin(AdministrativeUser head, String username, String email, String telephone, String password){
        if (head.getIsHead()){
            createAdministrator(username, email, telephone, password, false);
            return new Response.Builder(true).translatable("success.add.subadmin").build();
        } else{
            return new Response.Builder(false).translatable("failed.add.subadmin").build();
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

    public void freezeUser(PersonalUser user){
        user.setIsFrozen(true);
    }

    public void unfreezeUser(PersonalUser user){
        user.setIsFrozen(false);
        user.setRequestToUnfreeze(false);
    }

    public boolean removeUserItem(PersonalUser user, Long item){
        return (user.getInventory()).remove(item);
    }

    public Response confirmAddAllItemRequestForAUser(PersonalUser user) {
        for (Long item : user.getAddToInventoryRequest()) {
            user.addToInventory(item);
            user.getAddToInventoryRequest().remove(item);
        }
        return new Response.Builder(true).translatable("success.confirm.AddItem").build();
    }

    public Response confirmAddItemRequest(PersonalUser user, long item) {
        user.addToInventory(item);
        user.getAddToInventoryRequest().remove(item);
        return new Response.Builder(true).translatable("success.confirm.AddItem").build();
    }

    public Response confirmAddAllItemRequest(){
        while (needToConfirmAddItem.hasNext()){
            confirmAddAllItemRequestForAUser(needToConfirmAddItem.next());
        }
        return new Response.Builder(true).translatable("success.confirm.allAddItem").build();
    }

    public Iterator<PersonalUser> getUserRequestToUnfreeze(){
        return userRequestToUnfreeze;
    }

    public Response confirmUnfreezeUser(PersonalUser user){
        unfreezeUser(user);
        return new Response.Builder(true).translatable("success.confirm.unfreeze").build();
    }

    public Response confirmUnfreezeAllUser(){
        while (userRequestToUnfreeze.hasNext()){
            unfreezeUser(userRequestToUnfreeze.next());
        }
        return new Response.Builder(true).translatable("success.confirm.unfreezeAll").build();
    }

    public Response confirmFreezeUser(PersonalUser user) {
        freezeUser(user);
        return new Response.Builder(true).translatable("success.confirm.freeze").build();
    }

    public Response confirmFreezeAllUser(){
        while (needToFreezelist.hasNext()){
            currPersonalUser = needToFreezelist.next();
            freezeUser(currPersonalUser);
        }
        return new Response.Builder(true).translatable("success.confirm.freezeAll").build();
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

    public AdministrativeUser findAdminUser(String username) {
        return administrators.getFirst(
                AdministrativeUser -> AdministrativeUser.getUserName().equals(username));
    }



}
