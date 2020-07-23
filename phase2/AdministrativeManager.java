import group.item.Item;
import group.menu.data.Response;
import group.repository.Repository;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

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


    public AdministrativeManager(Repository<AdministrativeUser> administrativeUserRepository,
                                 Repository<PersonalUser> personalUserRepository, Repository<Trade> tradeRepository,
                                 Repository<Item> itemRepository){
        this.administrators = administrativeUserRepository;
        this.personalUserRepository = personalUserRepository;
        this.tradeRepository = tradeRepository;
        this.itemRepository = itemRepository;
        needToFreezelist = personalUserRepository.iterator(PersonalUser::getShouldBeFreezedUser);
        needToConfirmAddItem = personalUserRepository.iterator(PersonalUser::getAddToInventoryRequestIsNotEmpty);
        needToConfirmUnfreeze = personalUserRepository.iterator(PersonalUser::getRequestToUnfreeze);
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

    public Response getNeedToConfirmAddItemUserList(){
        StringBuilder stringBuilder = new StringBuilder();
        while (needToConfirmAddItem.hasNext()) {
            PersonalUser user = needToConfirmAddItem.next();
            stringBuilder.append("User ID: ").append(user.getUid()).append(" | Request IDs: ")
                    .append(user.getAddToInventoryRequest()).append("\n");
        }
        return new Response.Builder(true)
                .translatable("success.get.addItem", stringBuilder.toString()).build();
    }

    public Iterator<PersonalUser> getUserRequestToUnfreeze() {
        return needToConfirmUnfreeze;
    }

    public void freezeUser(PersonalUser user){
        user.setIsFrozen(true);
    }

    public void unfreezeUser(PersonalUser user){
        user.setIsFrozen(false);
        user.setRequestToUnfreeze(false);
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

    public Response confirmAddItemRequest(PersonalUser user, Integer item) {
        user.addToInventory(item);
        user.removeAddToInventoryRequest(item);
        // Item itemEntity = itemRepository.get(item);
        // itemRepository.add(itemEntity);
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
