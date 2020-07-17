package group.system;

import group.item.Item;
import group.item.ItemManager;
import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.PersonalUser;
import group.user.PersonalUserManager;

import java.util.ArrayList;
import java.util.Map;

public class UserController {

    private final Repository<PersonalUser> personalRepo;
    private final Repository<AdministrativeUser> adminRepo;
    final Repository<Item> itemRepo;
    private final PersonalUserManager personalUserManager;
    private final ItemManager itemManager;
    private final TradeController tradeController;
    private PersonalUser currUser;

    public UserController(ControllerDispatcher dispatcher) {
        personalRepo = dispatcher.personalUserRepository;
        adminRepo = dispatcher.adminUserRepository;
        itemRepo = dispatcher.itemRepository;
        personalUserManager = new PersonalUserManager(personalRepo, itemRepo);
        itemManager = new ItemManager(itemRepo);
        tradeController = dispatcher.tradeController;
    }

    public Response loginUser(Request request) {
        String username = request.get("username");
        String password = request.get("password");
        currUser = personalUserManager.getCurrPersonalUser(username, password);
        return personalUserManager.verifyLogin(username,password);
    }

    public Response registerUser(Request request) {
        String username = request.get("username");
        String email = request.get("email");
        String telephone = request.get("telephone");
        String password = request.get("password");
        return personalUserManager.createPersonalUser(username, email, telephone, password);
    }

    public Response removeItemFromInventory(Request request){
        Integer item = request.getInt("item");
        Item itemEntity = itemManager.findItemByUid(item);
        itemManager.remove(itemEntity);
        return personalUserManager.removeItemFromInventory(currUser, itemEntity);
    }

    public Response RequestAddNewItem(Request request){
        String item = request.get("item");
        String description = request.get("description");
        return personalUserManager.requestToAddItemToInventory(currUser, item, description);
    }

    public Response RequestUnfreeze(Request request) {
        return personalUserManager.UnfreezeRequest(currUser);
    }

    public Response AddItemToWishlist(Request request){
        String item = request.get("itemName");
        String description = request.get("description");
        return personalUserManager.addItemToWishlist(currUser, item, description);
    }

    public Response removeItemFromWishlist(Request request){
        Integer item = request.getInt("item");
        Item itemEntity = itemManager.findItemByUid(item);
        return personalUserManager.removeItemFromWishlist(currUser, itemEntity);
    }

    public Response browseAllItems(Request request) {
        return itemManager.browseItemsDisplay();
    }

    public Response browseInventory(Request request){
        return personalUserManager.getUserInventory(currUser);
    }

    public Response browseWishlist(Request request){
        return personalUserManager.getUserWishlist(currUser);
    }

    public Response checkFrozen(Request request) {
        return personalUserManager.getUserIsFrozen(currUser);

        //if (currUser.getIsFrozen()){
            //return new Response.Builder(false).translatable("frozen").build();
        //}
        //return new Response.Builder(true).translatable("not.frozen").build();
    }

    public Response topTraders(Request request){
        Map<Integer, Integer> frequentTraders = tradeController.getTradeFrequency(currUser.getUid());
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : frequentTraders.keySet()) {
            PersonalUser other = personalRepo.get(i);
            Integer times = frequentTraders.get(i);
            stringBuilder.append("Traded with ").append(other.toString()).append(times).append(" times.").append("\n");
        }
        return new Response.Builder(true).translatable("topTraders", stringBuilder.toString()).build();
    }

    public PersonalUser getCurrUser(){ return currUser; }
    public ItemManager getItemManager(){ return itemManager;}



}
