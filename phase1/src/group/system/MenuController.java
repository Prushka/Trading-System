package group.system;

import group.config.LoggerFactory;
import group.menu.MenuBuilder;
import group.menu.MenuBuilder.OperationType;
import group.menu.MenuBuilder.ValidatingType;
import group.menu.MenuLogicController;
import group.menu.data.Response;
import group.menu.persenter.ResponsePresenter;
import group.menu.validator.DateValidator;
import group.menu.validator.EnumValidator;
import group.menu.validator.RepositoryIdValidator;
import group.notification.SupportTicket;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {\__/}
 * (●_●)
 * ( >🌮
 */

// FIXME: this is the new format to handle success and failure in Response object:
//   menuBuilder.option(SupportTicket.class, OperationType.query, 2)
//       .submit("category", String::toUpperCase, new EnumValidator<>(SupportTicket.Category.class), ValidatingType.invalid, controller::getTicketsByCategory)
//       .succeeded("master.support.ticket").failed("master.account").master("master.support.trade","some.other.master");
//  NOTICE what will happen:
//  When Response success == true, the "master.support.ticket" will be the next menu
//  When Response success == false, the "master.account" will be the next menu
//  When you define "master.support.trade" in your response object, "master.support.trade" will be next. To use custom masters, they have to be put in here
//  In other words: if there's no method call: .master("master.support.trade"), you can't set it in Response as next node

public class MenuController {

    static final Logger LOGGER = new LoggerFactory(MenuController.class).getConfiguredLogger();

    private final MenuBuilder menuBuilder;

    private final List<Shutdownable> shutdowns;

    public MenuController() {
        menuBuilder = new MenuBuilder();
        shutdowns = new ArrayList<>();
    }


    // re building menu from scratch, see menu design at bottom of google doc
    public void mainMenu(UserController userController, AdministrativeUserController administrativeUserController) {
        menuBuilder.option(User.class, OperationType.verification, 1, "login")
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                //.input("email", null, ValidatingType.notexist) // if you want to check if the email exists directly in this input node, change the null to a lambda expression
                .submit("password", userController::loginUser)
                .succeeded("master.account").failed("master.account").master("master.account");

        menuBuilder.option(AdministrativeUser.class, OperationType.verification, 2, "login")
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                //.input("email", null, ValidatingType.notexist) // if you want to check if the email exists directly in this input node, change the null to a lambda expression
                .submit("password", administrativeUserController::loginAdminUser)
                .succeeded("master.account").failed("master.account").master("master.account");

        menuBuilder.option(User.class, OperationType.add, 3, "register")
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                .input("email", null, null, ValidatingType.invalid)
                .input("telephone", null, null, ValidatingType.invalid)
                .submit("password", password -> password.length() > 7, ValidatingType.invalid, userController::registerUser)
                .succeeded("master.account").failed("master.account").master("master.account");


        menuBuilder.option(AdministrativeUser.class, OperationType.add, 4, "register")
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                .input("email", null, null, ValidatingType.invalid)
                .input("telephone", null, null, ValidatingType.invalid)
                //.input("password", /*new PasswordEncryption(),*/ password -> password.length() > 7, ValidatingType.invalid) // the password encryption is broken,
                // you can put anything there if you want to process user input before it enters the Request object
                .submit("password", password -> password.length() > 7, ValidatingType.invalid, administrativeUserController::registerAdminUser)
                .succeeded("master.account").failed("master.account").master("master.account");

        menuBuilder.construct("master.account", true);

    }

    public void viewAccount(UserController userController) {

        menuBuilder.option(User.class, OperationType.verification, 1, "browseItems")
                .submit("browseAllItems", userController::browseAllItems)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.option(User.class, OperationType.add, 2, "wishlist")
                .submit("item", userController::AddItemToWishlist)
                .succeeded("master.view.account").failed("master.view.account").master("");

        menuBuilder.option(User.class, OperationType.remove, 3, "wishlist")
                .submit("itemname", userController::removeItemFromWishlist)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.option(User.class, OperationType.add, 4, "inventory")
                .input("item", null, ValidatingType.invalid)
                .submit("description", userController::RequestAddNewItem)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.option(User.class, OperationType.remove, 5, "inventory")
                .submit("itemname", userController::removeItemFromInventory)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.option(User.class, OperationType.add, 6, "requestUnfreeze")
                .submit("unfreeze", userController::RequestUnfreeze)
                .succeeded("master.view.account").failed("master.view.account").master("allItems");

        menuBuilder.construct("master.view.account", false);
    }





    /*

    public void adminUserAccess(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 1, "addSunadmin")
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                .input("email", null, null, ValidatingType.invalid)
                .input("telephone", null, null, ValidatingType.notexist)
                .input("password", new PasswordEncryption(), password -> password.length() > 8, ValidatingType.invalid)
                .submit("confirm", controller::addSubAdmin)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "findUser")
                .submit("username", name -> name.length() > 3, ValidatingType.invalid, controller::findUserForAdmin)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "findUser")
                .submit("confirm", name -> name.length() > 3, ValidatingType.invalid, controller::getFreezeUserList)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 3, "confirmFreezeUser")
                .submit("username", controller::confirmFreezeUser)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 4, "confirmFreezeAllUser")
                .submit("confirm", controller::confirmFreezeAllUser)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 5, "confirmUnFreezeUser")
                .submit("username", controller::confirmUnFreezeUser)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 6, "confirmUnFreezeAllUser")
                .submit("confirm", controller::confirmUnFreezeAllUser)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 7, "confirmAddItem")
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                .submit("item", controller::confirmAddItemRequest)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 8, "confirmAddItemAUser")
                .submit("username", controller::confirmAddAllItemRequestForAUser)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 9, "confirmAddAllUser")
                .submit("confirm", controller::confirmAddAllItemRequest)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 10, "removeItem")
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                .submit("item", controller::removeItemInUserInventory)
                .succeeded("master.adminAccess").failed("master.adminAccess").master("adminAccount");

        menuBuilder.construct("master.adminAccess", false);
    }*/


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

    public void supportTrade(TradeController controller) {
        // grace notes: keys correspond to request keys, .master calls the next set of nodes
        menuBuilder.option(Trade.class, OperationType.add, 1)
                .input("initiator", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.exists)
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
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .input("dateAndTime", String::toUpperCase, new DateValidator(), ValidatingType.invalid)
                .submit("confirm", controller::editMeetingDateAndTime)
                .succeeded("master.support.trade").failed("master.support.trade").master("submit.trade.represent",
                "failed.edit.trade");

        menuBuilder.option(Trade.class, OperationType.edit, 3, "Location")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .input("location", String::toUpperCase, null, ValidatingType.invalid)
                .submit("confirm", controller::editMeetingLocation)
                .succeeded("master.support.trade").failed("master.support.trade").master("submit.trade.represent",
                "failed.edit.trade");

        menuBuilder.option(Trade.class, OperationType.verification, 4, "Open")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .submit("confirm", controller::confirmingTradeOpen)
                .succeeded("master.support.trade").failed("master.support.trade").master("success.confirm.trade.open",
                "success.confirm.trade.wait", "failed.confirm.trade");

        menuBuilder.option(Trade.class, OperationType.verification, 5, "Complete")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .submit("confirm", controller::confirmingTradeComplete)
                .succeeded("master.support.trade").failed("master.support.trade").master("failed.confirm.trade",
                "success.confirm.trade.complete.perm", "success.confirm.trade.complete.temp",
                "success.confirm.trade.wait");

        menuBuilder.construct("master.support.trade", false);
    }

    public void present(Response response) {

    }

    public void runMenu() {
        MenuLogicController menu = new MenuLogicController(menuBuilder.constructFinal()); // the construct final will put all place holders to nodes
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        new ResponsePresenter(menu.fetchInitialResponse()).display();

        try {
            String input = "";
            while (!input.equalsIgnoreCase("exit")) {
                input = br.readLine();
                if (!input.equalsIgnoreCase("exit")) {
                    new ResponsePresenter(menu.parseInput(input)).display();
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to read from Buffered reader.", e);
        } catch (NullPointerException e) {
            LOGGER.log(Level.SEVERE, "There's no node next.", e);
        }
        shutdown();
    }

    public void shutdownHook(Shutdownable shutdownable) {
        this.shutdowns.add(shutdownable);
    }

    private void shutdown() {
        shutdowns.forEach(Shutdownable::shutdown);
    }

}
