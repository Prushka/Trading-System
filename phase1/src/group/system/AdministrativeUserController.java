package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.user.AdministrativeManager;
import group.user.AdministrativeUser;
import group.user.PersonalUser;
import group.item.ItemManager;

import java.util.Iterator;

public class AdministrativeUserController {

    private final Repository<PersonalUser> personalRepo;
    private final Repository<AdministrativeUser> adminRepo;
    private final AdministrativeManager administrativeManager;
    private AdministrativeUser currAdmin;

    public AdministrativeUserController(ControllerDispatcher dispatcher) {
        personalRepo = dispatcher.personalUserRepository;
        adminRepo = dispatcher.adminUserRepository;
        administrativeManager = new AdministrativeManager(adminRepo, personalRepo);
    }

    // Lucy comment: for now it is only for admin, will discuss how to put in personal
    // or separate personal and admin controller
    public Response loginAdminUser(Request request) {
        String username = request.get("username");
        String password = request.get("password");
        currAdmin = administrativeManager.getCurrAdmin(username, password);
        return administrativeManager.verifyLogin(username, password);
    }

    public Response registerAdminUser(Request request) {
        String username = request.get("username");
        String email = request.get("email");
        String telephone = request.get("telephone");
        String password = request.get("password");
        boolean isHead = request.getBoolean("isHead");
        return administrativeManager.createAdministrator(username, email, telephone, password, isHead);
    }

    public Response addSubAdmin(Request request){
        String username = request.get("username");
        String email = request.get("email");
        String telephone = request.get("telephone");
        String password = request.get("password");
        //boolean isHead = request.getBoolean("isHead");
        return administrativeManager.addSubAdmin(currAdmin, username, email, telephone, password);
    }

    public Response getFreezeUserList(Request request){
        return administrativeManager.getListUserShouldBeFreezed();
    }

    public Response findUserForAdmin(Request request){
        String username = request.get("username");
        return administrativeManager.findUserForAdmin(username);
    }

    public Response confirmFreezeUser(Request request){
        String username = request.get("username");
        PersonalUser user = administrativeManager.findUser(username);
        return administrativeManager.confirmFreezeUser(user);
    }

    public Response confirmFreezeAllUser(Request request){
        return administrativeManager.confirmFreezeAllUser();
    }

    public Response confirmUnFreezeUser(Request request){
        String username = request.get("username");
        PersonalUser user = administrativeManager.findUser(username);
        return administrativeManager.confirmUnfreezeUser(user);
    }

    public Response confirmUnFreezeAllUser(Request request){
        return administrativeManager.confirmUnfreezeAllUser();
    }

    public Response confirmAddAllItemRequestForAUser(Request request){
        String username = request.get("username");
        PersonalUser user = administrativeManager.findUser(username);
        return administrativeManager.confirmAddAllItemRequestForAUser(user);
    }

    public Response confirmAddAllItemRequest(Request request){
        return administrativeManager.confirmAddAllItemRequest();
    }

    public Response confirmAddItemRequest(Request request){
        String username = request.get("username");
        Long item = request.getLong("item");
        PersonalUser user = administrativeManager.findUser(username);
        // Item itemEntity = itemManager.get(item);
        // itemManager.add(itemEntity)
        return administrativeManager.confirmAddItemRequest(user, item);
    }

    public Response removeItemInUserInventory(Request request){
        String username = request.get("username");
        Long item = request.getLong("item");
        PersonalUser user = administrativeManager.findUser(username);
        // Item itemEntity = itemManager.get(item);
        // itemManager.remove(itemEntity)
        return administrativeManager.removeUserItem(user, item);
    }





}
