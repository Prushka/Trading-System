package group.user;

import group.item.Item;
import group.menu.data.Response;
import group.repository.Repository;

import java.util.*;

public class AdministrativeManager {


    private Repository<AdministrativeUser> administrators;
    private Repository<PersonalUser> personalUserRepository;
    private int transactionLimit = 100; //what is the init limit?
    private int lendBeforeBorrow = 1;
    private AdministrativeUser currAdmin = null;
    private PersonalUser currPersonalUser;
    private Iterator<PersonalUser> needToFreezelist;
    private Iterator<PersonalUser> needToConfirmAddItem;
    private Iterator<PersonalUser> needToConfirmUnfreeze;


    public AdministrativeManager(Repository<AdministrativeUser> administrativeUserRepository,
                                 Repository<PersonalUser> personalUserRepository){
        this.administrators = administrativeUserRepository;
        this.personalUserRepository = personalUserRepository;
        needToFreezelist = personalUserRepository.iterator(PersonalUser::getShouldBeFreezedUser);
        needToConfirmAddItem = personalUserRepository.iterator(PersonalUser::getAddToInventoryRequestIsNotEmpty);
        needToConfirmUnfreeze = personalUserRepository.iterator(PersonalUser::getRequestToUnfreeze);
    }

    public Response createAdministrator(String username, String email, String telephone, String password, boolean isHead){
        if (personalUserRepository.ifExists(
                PersonalUser -> PersonalUser.getUserName().equals(username))){
            return new Response.Builder(false).translatable("existed.username").build();
        }
        if (isHead){
        AdministrativeUser admin = new AdministrativeUser(username, email, telephone, password, isHead);
        administrators.add(admin);
        return new Response.Builder(true).translatable("success.create.new").build();
        }else{
            return new Response.Builder(false).translatable("not.head").build();
        }
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

    public Response getListUserShouldBeFreezed(){
        return new Response.Builder(true)
                .translatable("success.get.freeze", needToFreezelist).build();
    }

    public Response getNeedToConfirmAddItemUserList(){
        return new Response.Builder(true)
                .translatable("success.get.addItem", needToConfirmAddItem).build();
    }

    public Response getUserRequestToUnfreeze() {
        return new Response.Builder(true)
                .translatable("success.get.unfreezeRequest", needToConfirmUnfreeze).build();
    }

    public void freezeUser(PersonalUser user){
        user.setIsFrozen(true);
    }

    public void unfreezeUser(PersonalUser user){
        user.setIsFrozen(false);
        user.setRequestToUnfreeze(false);
    }

    public Response removeUserItem(PersonalUser user, Integer item){
        user.getInventory().remove(item);
        return new Response.Builder(true).translatable("success.remove.item").build();
    }


    public Response confirmAddAllItemRequestForAUser(PersonalUser user) {
        for (Integer item : user.getAddToInventoryRequest()) {
            user.addToInventory(item);
            user.getAddToInventoryRequest().remove(item);
        }
        return new Response.Builder(true).translatable("success.confirm.AddItem").build();
    }

    public Response confirmAddItemRequest(PersonalUser user, int item) {
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

    public Response confirmUnfreezeUser(PersonalUser user){
        unfreezeUser(user);
        return new Response.Builder(true).translatable("success.confirm.unfreeze").build();
    }

    public Response confirmUnfreezeAllUser(){
        while (needToConfirmUnfreeze.hasNext()){
            unfreezeUser(needToConfirmUnfreeze.next());
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

    public Response getTransactionLimit(){
        return new Response.Builder(true).translatable("success.get.tradeLimit", transactionLimit).build();
    }

    public Response setTransactionLimit(int limit){
        transactionLimit = limit;
        return new Response.Builder(true).translatable("success.set.tradeLimit").build();
    }

    public Response getLendBeforeBorrowLimit(){
        return new Response.Builder(true).translatable("success.get.borrowLimit", lendBeforeBorrow).build();
    }

    public Response setLendBeforeBorrowLimit(int limit){
        lendBeforeBorrow = limit;
        return new Response.Builder(true).translatable("success.set.borrowLimit").build();
    }

    public PersonalUser findUser(String username) {
            return personalUserRepository.getFirst(
                    PersonalUser -> PersonalUser.getUserName().equals(username));
    }

    public Response findUserForAdmin (String username) {
        if (personalUserRepository.ifExists(
                PersonalUser -> PersonalUser.getUserName().equals(username))) {
            PersonalUser foundUser = personalUserRepository.getFirst(
                    PersonalUser -> PersonalUser.getUserName().equals(username));
            return new Response.Builder(true).translatable("success.find.admin", foundUser).build();
        }
        return new Response.Builder(false).translatable("failed.find.user").build();
    }

    public Response findAdminUser(String username) {
        if (administrators.ifExists(
                AdministrativeUser -> AdministrativeUser.getUserName().equals(username))) {
            AdministrativeUser foundAdmin = administrators.getFirst(
                    AdministrativeUser -> AdministrativeUser.getUserName().equals(username));
            return new Response.Builder(true).translatable("success.find.admin", foundAdmin).build();
        }
        return new Response.Builder(false).translatable("failed.find.admin").build();
    }


}
