package group.system;

import group.menu.Menu;
import group.menu.MenuBuilder;
import group.menu.MenuBuilder.OperationType;
import group.menu.MenuBuilder.ValidatingType;
import group.menu.processor.PasswordEncryption;
import group.menu.validator.DateValidator;
import group.menu.validator.EnumValidator;
import group.menu.validator.RepositoryIdValidator;
import group.notification.SupportTicket;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.User;

import java.util.ArrayList;
import java.util.List;

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

public class MenuConstructor {

    private final MenuBuilder menuBuilder;

    private final List<Shutdownable> shutdowns;

    public MenuConstructor() {
        menuBuilder = new MenuBuilder();
        shutdowns = new ArrayList<>();
    }

    public void user(UserController controller) {
        // user login / register example
        menuBuilder.option(User.class, OperationType.verification, 1)
                .input("username", name -> name.length() > 3, ValidatingType.invalid )
                //.input("email", null, ValidatingType.notexist) // if you want to check if the email exists directly in this input node, change the null to a lambda expression
                .submit("password", controller::loginUser)
                .succeeded("master.support.trade").failed("master.account").master("master.account");

        menuBuilder.option(User.class, OperationType.add, 2)
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                .input("email", null, null, ValidatingType.invalid)
                .input("telephone", null, null, ValidatingType.invalid)
                .input("password", new PasswordEncryption(), password -> password.length() > 8, ValidatingType.invalid) // the password encryption is broken,
                // you can put anything there if you want to process user input before it enters the Request object
                .submit("confirm", controller::registerUser)
                .succeeded("master.support.trade").failed("master.account").master("master.account");
        // submit node can be password, if you don't want the user to confirm their input. doing so users will directly submit their input in the password part

        menuBuilder.construct("master.account", true);
    }

    public void userRequest(UserController controller){

        menuBuilder.option(User.class, OperationType.add, 1)
                .input("item",null, null, ValidatingType.invalid)
                .input("description", null, null, ValidatingType.invalid)
                .submit("unfreeze", controller::RequestAddNewItem)
                .master("master.account");


        menuBuilder.option(User.class, OperationType.add, 2)
                .submit("unfreeze", controller::RequestUnfreeze)
                .master("master.account");

    }


    public void adminUser(AdministrativeUserController controller) {

        menuBuilder.option(AdministrativeUser.class, OperationType.verification, 1)
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                //.input("email", null, ValidatingType.notexist) // if you want to check if the email exists directly in this input node, change the null to a lambda expression
                .submit("password", controller::loginAdminUser)
                .master("master.adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 2, "addSunadmin")
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                .input("email", null, null, ValidatingType.invalid)
                .input("telephone", null, null, ValidatingType.notexist)
                .input("password", new PasswordEncryption(), password -> password.length() > 8, ValidatingType.invalid)
                .submit("confirm", controller::addSubAdmin)
                .master("master.adminAccount");

        menuBuilder.option(AdministrativeUser.class, OperationType.add, 2)
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                .input("email", null, null, ValidatingType.invalid)
                .input("telephone", null, null, ValidatingType.invalid)
                .input("password", new PasswordEncryption(), password -> password.length() > 8, ValidatingType.invalid) // the password encryption is broken,
                // you can put anything there if you want to process user input before it enters the Request object
                .submit("confirm", controller::registerAdminUser)
                .master("master.account");

        menuBuilder.construct("master.adminAccount", true);
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

    // change this grace code when the actually controller comes in
    public void supportTrade(TestTradeController controller){
        // grace notes: keys correspond to request keys, .master calls the next set of nodes
        menuBuilder.option(Trade.class, OperationType.add, 1)
                .input("initiator", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.exists)
                .input("respondent", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .input("lendingItem", null, null, ValidatingType.invalid)
                .input("borrowingItem", null, null, ValidatingType.invalid)
                .input("isPermanent", String::toLowerCase, null, ValidatingType.invalid)
                .input("dateAndTime", String::toUpperCase, new DateValidator(), ValidatingType.invalid)
                .input("location", String::toUpperCase, null, ValidatingType.invalid)
                .submit("confirm", controller::addTrade)
                .succeeded("master.support.trade").failed("master.support.trade").master("master.support.trade");

        menuBuilder.option(Trade.class, OperationType.edit, 2, "Date/Time")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .input("dateAndTime", String::toUpperCase, new DateValidator(), ValidatingType.invalid)
                .submit("confirm", controller::editMeetingDateAndTime)
                .succeeded("master.support.trade").failed("master.support.trade").master("master.support.trade");

        menuBuilder.option(Trade.class, OperationType.edit, 3, "Location")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .input("location", String::toUpperCase, null, ValidatingType.invalid)
                .submit("confirm", controller::editMeetingLocation)
                .succeeded("master.support.trade").failed("master.support.trade").master("master.support.trade");

        menuBuilder.option(Trade.class, OperationType.verification, 4, "Open")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .submit("confirm", controller::confirmingTradeOpen)
                .succeeded("master.support.trade").failed("master.support.trade").master("master.support.trade");

        menuBuilder.option(Trade.class, OperationType.verification, 5, "Complete")
                .input("tradeID", null, new RepositoryIdValidator(controller.tradeRepository),
                        ValidatingType.exists)
                .input("editingUser", null, new RepositoryIdValidator(controller.personalUserRepository),
                        ValidatingType.invalid)
                .submit("confirm", controller::confirmingTradeComplete)
                .succeeded("master.support.trade").failed("master.support.trade").master("master.support.trade");

        menuBuilder.construct("master.support.trade", false);
    }

    public void runMenu() {
        ConsoleSystem console = new ConsoleSystem();
        console.run(new Menu(menuBuilder.constructFinal())); // the construct final will put all place holders to nodes
        shutdown();
    }

    public void shutdownHook(Shutdownable shutdownable) {
        this.shutdowns.add(shutdownable);
    }

    private void shutdown() {
        shutdowns.forEach(Shutdownable::shutdown);
    }

}
