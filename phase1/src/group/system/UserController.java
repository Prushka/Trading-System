package group.system;

import group.item.ItemManager;
import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.user.*;
import group.item.Item;

import java.util.Iterator;

public class UserController {

    private final Repository<PersonalUser> personalRepo;
    private final Repository<AdministrativeUser> adminRepo;
    private final Repository<Item> itemRepo;
    private final PersonalUserManager personalUserManager;
    private final ItemManager itemManager;
    private PersonalUser currUser;

    public UserController(ControllerDispatcher dispatcher) {
        personalRepo = dispatcher.personalUserRepository;
        adminRepo = dispatcher.adminUserRepository;
        itemRepo = dispatcher.itemRepository;
        personalUserManager = new PersonalUserManager(personalRepo);
        itemManager = new ItemManager(itemRepo);
        dispatcher.menuConstructor.user(this);
    }

    public Response loginUser(Request request) {
        String username = request.get("username");
        String password = request.get("password");
        currUser= personalUserManager.getCurrPersonalUser(username, password);
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
        Long item = request.getLong("item");
        Item itemEntity = itemManager.findItemByUid(item);
        itemManager.remove(itemEntity);
        return personalUserManager.removeItemFromInventory(currUser, item);
    }

    public Response RequestAddNewItem(Request request){
        String item = request.get("item");
        String description = request.get("description");
        return personalUserManager.createNewItemAndRequestAdd(currUser, item, description);
    }

    public Response RequestUnfreeze(Request request) {
        return personalUserManager.UnfreezeRequest(currUser);
    }

    public Response AddItemToWishlist(Request request){
        Long item = request.getLong("item");
        return personalUserManager.addItemToWishlist(currUser, item);
    }

    public Response removeItemFromWishlist(Request request){
        Long item = request.getLong("item");
        return personalUserManager.removeItemFromInventory(currUser, item);
    }




    public Response browseAllItems(Request request) {
        return itemManager.browseItemsDisplay();
    }




}
