package group.system;

import group.item.Item;
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
    private final Repository<Item> itemRepo;
    private AdministrativeUser currAdmin;

    public AdministrativeUserController(ControllerDispatcher dispatcher) {
        personalRepo = dispatcher.personalUserRepository;
        adminRepo = dispatcher.adminUserRepository;
        itemRepo = dispatcher.itemRepository;
        administrativeManager = new AdministrativeManager(adminRepo, personalRepo, dispatcher.tradeRepository, itemRepo);
    }

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
        boolean isHead;
        if (request.get("isHead").equalsIgnoreCase("yes")) {
            isHead = true;
        } else {
            isHead = false;
        }
        return administrativeManager.createAdministrator(username, email, telephone, password, isHead);
    }

    public Response addSubAdmin(Request request) {
        String username = request.get("username");
        String email = request.get("email");
        String telephone = request.get("telephone");
        String password = request.get("password");
        return administrativeManager.addSubAdmin(currAdmin, username, email, telephone, password);
    }

    public Response viewFreezeUserList(Request request) {
        return administrativeManager.getListUserShouldBeFreezed();
    }

    public Response viewAddItemRequest(Request request) {
        return administrativeManager.getNeedToConfirmAddItemUserList();
    }

    public Response viewUnfreezeRequest(Request request) {
        return administrativeManager.getUserRequestToUnfreeze();
    }

    public Response findUserForAdmin(Request request) {
        String username = request.get("username");
        return administrativeManager.findUserForAdmin(username);
    }

    public Response confirmFreezeUser(Request request) {
        String username = request.get("username");
        PersonalUser user = administrativeManager.findUser(username);
        return administrativeManager.confirmFreezeUser(user);
    }

    public Response confirmFreezeAllUser(Request request) {
        return administrativeManager.confirmFreezeAllUser();
    }

    public Response confirmUnFreezeUser(Request request) {
        String username = request.get("username");
        PersonalUser user = administrativeManager.findUser(username);
        return administrativeManager.confirmUnfreezeUser(user);
    }

    public Response confirmUnFreezeAllUser(Request request) {
        return administrativeManager.confirmUnfreezeAllUser();
    }

    public Response confirmAddAllItemRequestForAUser(Request request) {
        String username = request.get("username");
        PersonalUser user = administrativeManager.findUser(username);
        return administrativeManager.confirmAddAllItemRequestForAUser(user);
    }

    public Response confirmAddAllItemRequest(Request request) {
        return administrativeManager.confirmAddAllItemRequest();
    }

    public Response confirmAddItemRequest(Request request) {
        String username = request.get("username");
        Integer item = request.getInt("item");
        PersonalUser user = administrativeManager.findUser(username);
        Item itemEntity = itemRepo.get(item);
        return administrativeManager.confirmAddItemRequest(user, item);
    }

    public Response removeItemInUserInventory(Request request) {
        String username = request.get("username");
        Integer item = request.getInt("item");
        PersonalUser user = administrativeManager.findUser(username);
        Item itemEntity = itemRepo.get(item);
        return administrativeManager.removeUserItem(user, item);
    }

    public Response setTransactionLimit(Request request) {
        int limit = request.getInt("limit");
        return administrativeManager.setTransactionLimit(limit);
    }

    public Response setLendBeforeBorrowLimit(Request request) {
        int limit = request.getInt("limit");
        return administrativeManager.setLendBeforeBorrowLimit(limit);
    }

    public Response viewTransactionLimit(Request request) {
        //int limit = request.getInt("limit");
        return administrativeManager.getTransactionLimit();
    }

    public Response viewLendBeforeBorrowLimit(Request request) {
        //int limit = request.getInt("limit");
        return administrativeManager.getLendBeforeBorrowLimit();
    }


}
