package phase2.trade.system;

import group.menu.data.Request;
import group.menu.data.Response;
import Repository;
import group.user.AdministrativeManager;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

public class AdministrativeUserController {

    private final Repository<PersonalUser> personalRepo;
    private final AdministrativeManager administrativeManager;
    private AdministrativeUser currAdmin;

    public AdministrativeUserController(ControllerDispatcher dispatcher) {
        personalRepo = dispatcher.personalUserRepository;
        administrativeManager = new AdministrativeManager(dispatcher.adminUserRepository, dispatcher.personalUserRepository, dispatcher.tradeRepository, dispatcher.itemRepository);
    }

    public Response loginUser(Request request) {
        return loginUser(request.get("username"), request.get("password"));
    }

    public Response loginUser(String username, String password) {
        currAdmin = administrativeManager.getCurrAdmin(username, password);
        return administrativeManager.verifyLogin(username, password);
    }

    public Response registerAdminUser(Request request) {
        String username = request.get("username");
        String email = request.get("email");
        String telephone = request.get("telephone");
        String password = request.get("password");
        boolean isHead;
        isHead = request.get("isHead").equalsIgnoreCase("yes");
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
        int username = request.getInt("fine.username");
        Integer item = request.getInt("phase2/trade/item");
        PersonalUser user = personalRepo.get(username);
        // Item itemEntity = itemRepo.get(com.phase2.com.phase2.trade.trade.item);
        return administrativeManager.confirmAddItemRequest(user, item);
    }

    public Response removeItemInUserInventory(Request request) {
        int username = request.getInt("fine.username");
        Integer item = request.getInt("phase2/trade/item");
        PersonalUser user = personalRepo.get(username);
        // Item itemEntity = itemRepo.get(com.phase2.com.phase2.trade.trade.item);
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
