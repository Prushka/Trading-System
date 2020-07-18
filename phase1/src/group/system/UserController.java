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
import java.util.List;
import java.util.Map;

public class UserController {

    // TODO: What's wrong **** ***
    private final Repository<PersonalUser> personalRepo;
    private final Repository<Item> itemRepo;
    private final PersonalUserManager personalUserManager;
    private final ItemManager itemManager;
    private final TradeController tradeController;

    public UserController(ControllerDispatcher dispatcher) {
        personalRepo = dispatcher.personalUserRepository;
        itemRepo = dispatcher.itemRepository;
        tradeController = dispatcher.tradeController;
        personalUserManager = new PersonalUserManager(personalRepo, itemRepo);
        itemManager = new ItemManager(itemRepo);
    }

    public Response loginUser(Request request) {
        String username = request.get("username");
        String password = request.get("password");
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
        Integer item = request.getInt("itemname");
        Item itemEntity = itemManager.findItemByUid(item);
        Integer itemID = itemEntity.getUid();
        itemManager.removeAvailable(itemID);
        itemManager.remove(itemEntity);
        return personalUserManager.removeItemFromInventory(getCurrUser(), itemID);
    }

    public Response RequestAddNewItem(Request request){
        String item = request.get("item");
        String description = request.get("description");
        return personalUserManager.requestToAddItemToInventory(getCurrUser(), item, description);
    }

    public Response RequestUnfreeze(Request request) {
        return personalUserManager.UnfreezeRequest(getCurrUser());
    }

    public Response AddItemToWishlist(Request request){
        String item = request.get("itemName");
        String description = request.get("description");
        return personalUserManager.addItemToWishlist(getCurrUser(), item, description);
    }

    public Response removeItemFromWishlist(Request request){
        Integer item = request.getInt("itemname");
        Item itemEntity = itemManager.findItemByUid(item);
        return personalUserManager.removeItemFromWishlist(getCurrUser(), itemEntity);
    }

    public Response browseAllItems(Request request) {
        return itemManager.browseItemsDisplay();
    }

    public Response browseInventory(Request request){
        List<Integer> inventory =  personalUserManager.getUserInventory(getCurrUser());
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : inventory) {
            Item item = itemRepo.get(i);
            stringBuilder.append(item.toString()).append("\n");
        }
        return new Response.Builder(true).
                translatable("success.get.inventory", stringBuilder.toString()).build();
    }

    public Response browseWishlist(Request request){
        List<Integer> wish = personalUserManager.getUserWishlist(getCurrUser());
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : wish) {
            Item item = itemRepo.get(i);
            stringBuilder.append("ItemID: ").append(item.getUid()).append(" | Description: ").append(item.getItemName())
                    .append("- ").append(item.getDescription()).append("\n");
        }
        return new Response.Builder(true).
                translatable("success.get.wishlist", stringBuilder.toString()).build();
    }

    public Response checkFrozen(Request request) {
        return personalUserManager.getUserIsFrozen(getCurrUser());

        //if (currUser.getIsFrozen()){
            //return new Response.Builder(false).translatable("frozen").build();
        //}
        //return new Response.Builder(true).translatable("not.frozen").build();
    }

    public Response topTraders(Request request){
        Map<Integer, Integer> frequentTraders = tradeController.getTradeFrequency(getCurrUser().getUid());
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : frequentTraders.keySet()) {
            PersonalUser other = personalRepo.get(i);
            Integer times = frequentTraders.get(i);
            stringBuilder.append("Traded with ").append(other.toString()).append(times).append(" times.").append("\n");
        }
        return new Response.Builder(true).translatable("topTraders", stringBuilder.toString()).build();
    }

    public PersonalUser getCurrUser(){ return personalUserManager.getCurrUser(); }
    public ItemManager getItemManager(){ return itemManager;}



}
