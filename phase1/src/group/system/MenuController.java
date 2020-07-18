package group.system;

import group.menu.MenuBuilder;
import group.menu.MenuBuilder.OperationType;
import group.menu.MenuBuilder.ValidatingType;
import group.menu.MenuLogicController;
import group.menu.processor.PasswordEncryption;
import group.menu.validator.*;
import group.notification.SupportTicket;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.PersonalUser;
import group.user.User;

/**
 * {\__/}
 * (●_●)
 * ( >🌮
 */

// TODO: succeeded / failed / master can all be ignored if you don't use them.
//   The addon can also be ignored as int as there are no two options that have same class and operation type
// When Response success == true, the "master.support.ticket" will be the next menu
// When Response success == false, the "master.account" will be the next menu
// When you define "master.support.trade" in your response object, "master.support.trade" will be next. To use custom masters, they have to be put in here

public class MenuController {

    private final MenuBuilder menuBuilder;
    private final ValidatorFactory validatorFactory;

    public MenuController() {
        menuBuilder = new MenuBuilder();
        validatorFactory = new ValidatorFactory();
    }

    // re building menu from scratch, see menu design at bottom of google doc
    public void mainMenu(UserController userController, AdministrativeUserController administrativeUserController) {
        menuBuilder.option(User.class, OperationType.verification, 1, "loginUser")
                .input("username")
                .submit("password",new PasswordEncryption(),null,ValidatingType.invalid, userController::loginUser)
                .succeeded("master.userAccess").failed("master.account").master("master.account");

        menuBuilder.option(User.class, OperationType.add, 2, "register")
                .input("username", validatorFactory.getValidator(ValidatorFactory.Type.USER_NAME))
                .input("email", new EmailValidator())
                .input("telephone", validatorFactory.getValidator(ValidatorFactory.Type.TELEPHONE))
                .submit("password", new PasswordEncryption(), validatorFactory.getValidator(ValidatorFactory.Type.PASSWORD), ValidatingType.invalid, userController::registerUser)
                .succeeded("master.account").failed("master.account").master("master.account");


        menuBuilder.option(AdministrativeUser.class, OperationType.add, 3, "register")
                .input("username", validatorFactory.getValidator(ValidatorFactory.Type.USER_NAME))
                .input("email", new EmailValidator())
                .input("telephone", validatorFactory.getValidator(ValidatorFactory.Type.TELEPHONE))
                .input("password", new PasswordEncryption(), validatorFactory.getValidator(ValidatorFactory.Type.PASSWORD), ValidatingType.invalid)
                .submit("isHead", administrativeUserController::registerAdminUser)
                .succeeded("master.account").failed("master.account").master("master.account");

        menuBuilder.construct("master.account", true);

    }

    public void personalUserAccess(UserController controller, SupportTicketController supportController) {

        menuBuilder.option(PersonalUser.class, OperationType.query, 1, "account")
                .skippableSubmit(controller::checkFrozen).succeeded("master.view.account").failed("master.view.account").master("master.account");

        menuBuilder.option(PersonalUser.class, OperationType.query, 2, "trade")
                .skippableSubmit(controller::checkFrozen).succeeded("master.userAccess").failed("master.support.trade").master("master.account");

        /*
        menuBuilder.option(SupportTicket.class, OperationType.add, 3)
                .input("content", supportController::ifTicketContentNotExist, ValidatingType.exists)
                .input("category", String::toUpperCase, new EnumValidator<>(SupportTicket.Category.class), ValidatingType.invalid)
                .input("priority", String::toUpperCase, new EnumValidator<>(SupportTicket.Priority.class), ValidatingType.invalid)
                .submit("confirm", supportController::addTicket)
                .succeeded("master.userAccess");
         */

        menuBuilder.construct("master.userAccess", false);
    }

    public void viewAccount(UserController userController, TradeController tradeController) {

        menuBuilder.option(User.class, OperationType.verification, 1, "browseItems")
                .skippableSubmit(userController::browseAllItems).succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.option(User.class, OperationType.view, 2, "wishlist")
                .skippableSubmit(userController::browseWishlist).succeeded("master.view.account").failed("master.view.account").master("success.get.wishlist");

        menuBuilder.option(User.class, OperationType.add, 3, "wishlist")
                .input("itemName", null, null, ValidatingType.invalid)
                .submit("description", userController::AddItemToWishlist)
                .succeeded("master.view.account").failed("master.view.account").master("success.add.wishlist");

        menuBuilder.option(User.class, OperationType.remove, 4, "wishlist")
                .submit("itemname",null, userController::removeItemFromWishlist)
                .succeeded("master.view.account").failed("master.view.account").master("success.remove.wishlist");

        menuBuilder.option(User.class, OperationType.view, 5, "inventory")
                .skippableSubmit(userController::browseInventory).succeeded("master.view.account").failed("master.view.account").master("success.get.inventory");

        menuBuilder.option(User.class, OperationType.add, 6, "inventory")
                .input("item", null, ValidatingType.invalid)
                .submit("description", userController::RequestAddNewItem)
                .succeeded("master.view.account").failed("master.view.account").master("success.request.addItem");

        menuBuilder.option(User.class, OperationType.remove, 7, "inventory")
                .submit("itemname", userController::removeItemFromInventory)
                .succeeded("master.view.account").failed("master.view.account").master("success.remove.item");

        menuBuilder.option(User.class, OperationType.add, 8, "requestUnfreeze")
                .submit("unfreeze", userController::RequestUnfreeze)
                .succeeded("master.view.account").failed("master.view.account").master("success.request.unfreeze");

        menuBuilder.option(Trade.class, OperationType.view, 9, "all")
                .skippableSubmit(tradeController::getAllTrades)
                .succeeded("master.view.account").failed("master.view.account").master("allTrades");

        menuBuilder.option(User.class, OperationType.view, 10, "recentTrades")
                .skippableSubmit(tradeController::getRecentTrades).succeeded("master.view.account").failed("master.view.account").master("recentTrades");

        menuBuilder.option(Trade.class, OperationType.view, 11, "frequentTrades")
                .skippableSubmit(userController::topTraders).succeeded("master.view.account").failed("master.view.account").master("topTraders");

        menuBuilder.option(Trade.class, OperationType.query, 12, "return")
                .skippableSubmit(tradeController::skip).succeeded("master.userAccess");

        menuBuilder.construct("master.view.account", false);
    }

    public void supportTrade(TradeController controller) {
        menuBuilder.option(Trade.class, OperationType.add, 1)
                .input("respondent", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.exists)
                .input("lendingItem", null, controller::isAnItem, ValidatingType.invalid)
                .input("borrowingItem", null, controller::isAnItem, ValidatingType.invalid)
                .input("isPermanent", String::toLowerCase, controller::isBool, ValidatingType.invalid)
                .input("dateAndTime", String::toUpperCase, new DateValidator(), ValidatingType.invalid)
                .input("location", String::toUpperCase, null, ValidatingType.invalid)
                .submit("confirm", controller::addTrade)
                .succeeded("master.support.trade").failed("master.support.trade").master("submit.trade.represent",
                "failed.create.trade");

        menuBuilder.option(Trade.class, OperationType.edit, 2, "Date/Time")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("dateAndTime", String::toUpperCase, new DateValidator(), ValidatingType.invalid)
                .submit("confirm", controller::editMeetingDateAndTime)
                .succeeded("master.support.trade").failed("master.support.trade").master("submit.trade.represent",
                "failed.edit.trade");

        menuBuilder.option(Trade.class, OperationType.edit, 3, "Location")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("location", String::toUpperCase, null, ValidatingType.invalid)
                .submit("confirm", controller::editMeetingLocation)
                .succeeded("master.support.trade").failed("master.support.trade").master("submit.trade.represent",
                "failed.edit.trade");

        menuBuilder.option(Trade.class, OperationType.verification, 4, "Open")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .submit("confirm", controller::confirmingTradeOpen)
                .succeeded("master.support.trade").failed("master.support.trade").master("success.confirm.trade.open",
                "success.confirm.trade.wait", "failed.confirm.trade");

        menuBuilder.option(Trade.class, OperationType.verification, 5, "Complete")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .submit("confirm", controller::confirmingTradeComplete)
                .succeeded("master.support.trade").failed("master.support.trade").master("failed.confirm.trade",
                "success.confirm.trade.complete.perm", "success.confirm.trade.complete.temp",
                "success.confirm.trade.wait");

        menuBuilder.option(Trade.class, OperationType.query, 6, "returnAgain")
                .skippableSubmit(controller::skip).succeeded("master.userAccess");

        menuBuilder.construct("master.support.trade", false);
    }

    public void supportTicket(SupportTicketController controller) {
        menuBuilder.option(SupportTicket.class, OperationType.add, 1)
                .input("content", controller::ifTicketContentNotExist, ValidatingType.exists)
                .input("category", String::toUpperCase, new EnumValidator<>(SupportTicket.Category.class), ValidatingType.invalid)
                .input("priority", String::toUpperCase, new EnumValidator<>(SupportTicket.Priority.class), ValidatingType.invalid)
                .submit("confirm", controller::addTicket)
                .succeeded("master.support.ticket");

        menuBuilder.option(SupportTicket.class, OperationType.query, 2)
                .submit("category", String::toUpperCase, new EnumValidator<>(SupportTicket.Category.class), ValidatingType.invalid, controller::getTicketsByCategory)
                .succeeded("master.support.ticket").failed("master.account").master("master.support.trade");

        menuBuilder.construct("master.support.ticket", false);
    }

    public void adminUserAccess(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 1, "addSunadmin")
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                .input("email", null, null, ValidatingType.invalid)
                .input("telephone", null, null, ValidatingType.notexist)
                .submit("password", password -> password.length() > 8, ValidatingType.invalid, controller::addSubAdmin)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccess");

        /*menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "findUser")
                .submit("username", null, ValidatingType.notexist, controller::findUserForAdmin)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");*/

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "confirmAdd")
                .skippableSubmit(controller::loginUser)
                .succeeded("master.adminUserAddItemAccess").failed("master.adminAccess").master("adminAccess");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 3, "removeItem")
                .input("fine.username", null, ValidatingType.notexist)
                .submit("item", controller::removeItemInUserInventory)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccess");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 4, "confirmFreeze")
                .skippableSubmit(controller::loginUser)
                .succeeded("master.adminUserFreezeAccess").failed("master.adminAccess").master("adminAccess");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 5, "confirmUnfreeze")
                .skippableSubmit(controller::loginUser)
                .succeeded("master.adminUserUnfreezeAccess").failed("master.adminAccess").master("adminAccess");

        menuBuilder.option(AdministrativeUser.class, OperationType.edit, 6, "limit")
                .skippableSubmit(controller::loginUser)
                .succeeded("master.adminUserLimitAccess").failed("master.adminAccess").master("adminAccess");

        menuBuilder.construct("master.adminAccess", false);
    }


    public void adminUserAddItemAccess(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.view, 1, "allAddItemRequest")
                .skippableSubmit(controller::viewAddItemRequest)
                .succeeded("master.adminUserAddItemAccess").failed("master.adminUserAddItemAccess").master("success.get.addItem");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "confirmAddItem")
                .input("fine.username", null, ValidatingType.notexist)
                .submit("item", controller::confirmAddItemRequest)
                .succeeded("master.adminUserAddItemAccess").failed("master.adminUserAddItemAccess").master("adminAccount");
/*
        menuBuilder.option(AdministrativeUser.class, OperationType.add, 3, "confirmAddItemAUser")
                .submit("confirm", controller::confirmAddAllItemRequestForAUser)
                .succeeded("master.adminUserAddItemAccess").failed("master.adminUserAddItemAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 4, "confirmAddAllUser")
                .submit("confirm", controller::confirmAddAllItemRequest)
                .succeeded("master.adminUserAddItemAccess").failed("master.adminUserAddItemAccess").master("adminAccount");
*/
        menuBuilder.construct("master.adminUserAddItemAccess", false);
    }


    public void adminUserFreezeAccess(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.view, 1, "freezelist")
                .skippableSubmit(controller::viewFreezeUserList)
                .succeeded("master.adminUserFreezeAccess").failed("master.adminUserFreezeAccess").master("success.get.freeze");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "confirmFreezeUser")
                .submit("username", controller::confirmFreezeUser)
                .succeeded("master.adminUserFreezeAccess").failed("master.adminUserFreezeAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 3, "confirmFreezeAllUser")
                .submit("confirm", controller::confirmFreezeAllUser)
                .succeeded("master.adminUserFreezeAccess").failed("master.adminUserFreezeAccess").master("adminAccount");

        menuBuilder.construct("master.adminUserFreezeAccess", false);
    }

    public void adminUserUnfreezeAccess(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.view, 1, "unfreezeRequest")
                .skippableSubmit(controller::viewUnfreezeRequest)
                .succeeded("master.adminUserUnfreezeAccess").failed("master.adminUserUnfreezeAccess").master("success.get.unfreezeRequest");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "confirmUnFreezeUser")
                .submit("username", controller::confirmUnFreezeUser)
                .succeeded("master.adminUserUnfreezeAccess").failed("master.adminUserUnfreezeAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 3, "confirmUnFreezeAllUser")
                .submit("confirm", controller::confirmUnFreezeAllUser)
                .succeeded("master.adminUserUnfreezeAccess").failed("master.adminUserUnfreezeAccess").master("adminAccount");

        menuBuilder.construct("master.adminUserUnfreezeAccess", false);
    }


    public void adminUserLimitAccess(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.view, 1, "viewLendBeforeBorrowLimit")
                .skippableSubmit(controller::viewLendBeforeBorrowLimit)
                .succeeded("master.adminLimitAccess").failed("master.adminLimitAccess").master("success.get.borrowLimit");


        menuBuilder.option(AdministrativeUser.class, OperationType.view, 2, "TransLimit")
                .skippableSubmit(controller::viewTransactionLimit)
                .succeeded("master.adminLimitAccess").failed("master.adminLimitAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.edit, 3, "LandBeforeBorrowLimit")
                .submit("limit", controller::setLendBeforeBorrowLimit)
                .succeeded("master.adminLimitAccess").failed("master.adminLimitAccess").master("success.get.tradeLimit");

        menuBuilder.option(AdministrativeUser.class, OperationType.edit, 4, "TransLimit")
                .submit("limit", controller::setTransactionLimit)
                .succeeded("master.adminLimitAccess").failed("master.adminLimitAccess").master("adminAccount");

        menuBuilder.construct("master.adminUserLimitAccess", false);
    }

    public MenuLogicController generateMenuLogicController() {
        return new MenuLogicController(menuBuilder.constructFinal());
    }


}
