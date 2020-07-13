package group.system;

import group.menu.Menu;
import group.menu.MenuBuilder;
import group.menu.MenuBuilder.OperationType;
import group.menu.MenuBuilder.ValidatingType;
import group.menu.processor.PasswordEncrypt;
import group.menu.validator.EnumValidator;
import group.notification.SupportTicket;
import group.trade.Trade;
import group.user.User;

import java.util.ArrayList;
import java.util.List;

public class MenuConstructor {

    private final MenuBuilder menuBuilder;

    private final List<Shutdownable> shutdowns;

    public MenuConstructor() {
        menuBuilder = new MenuBuilder();
        shutdowns = new ArrayList<>();
    }

    public void user(UserController controller) {
        // user login / register example
        menuBuilder.option(User.class, OperationType.add, 1)
                .input("username", name -> name.length() > 3, ValidatingType.invalid)
                .input("password", new PasswordEncrypt(), password -> password.length() > 8, ValidatingType.invalid) // the password encryption is broken,
                // you can put anything there if you want to process user input before it enters the Request object
                .input("email")
                .submit("confirm", controller::loginUser);

        menuBuilder.option(User.class, OperationType.verification, 2)
                .input("email", null, ValidatingType.notexist) // if you want to check if the email exists directly in this input node, change the null to a lambda expression
                .input("password", password -> password.length() > 8, ValidatingType.invalid)
                .submit("confirm", controller::registerUser);
        // submit node can be password, if you don't want the user to confirm their input. doing so users will directly submit their input in the password part

        menuBuilder.construct("master.account"); // this one should be the root, but many things are unimplemented
        // You have to call construct before creating a new master option node.

        /* Menu Structure:
         master option node has option nodes as children, they will be displayed together. You don't need to worry about the order since they are sorted by option nodes' ids when building
         option nodes have input nodes as children
         submit nodes will be the child of an input node or option node
         Finally submit node can have a master option node as a child.

         (For example:
         If the user logs in, the submit node receives a successful Response object,
          then another master option node with options will be displayed for users to choose from)

         This is the way I think a menu level should be constructed. So you can only do this in MenuFactory. There's more freedom if you want to build nodes directly.
         (The MenuFactory class is not perfect for now (it's a mess :(
         */

        /* Language & Text:
        If you use the menu factory, the identifier will be the combination of {OperationType, Input Key, Validating Type, Class} etc.

        If you create nodes directly, you have to pass identifiers manually as parameters. You also need to read about all builders.

        You can generate the language.properties by calling menuFactory.generateLanguage("language"); if you are using menu factory.
        This will put all identifiers in the properties file with undefined value
         */
    }

    public void supportTicket(SupportTicketController controller) {
        menuBuilder.option(SupportTicket.class, OperationType.add, 1)
                .input("content", controller::ifTicketContentNotExist, ValidatingType.exists)
                .input("category", String::toUpperCase, new EnumValidator<>(SupportTicket.Category.class), ValidatingType.invalid)
                .input("priority", String::toUpperCase, new EnumValidator<>(SupportTicket.Priority.class), ValidatingType.invalid)
                .submit("confirm", controller::addTicket)
                .master("master.support.ticket");

        menuBuilder.option(SupportTicket.class, OperationType.query, 2)
                .submit("category", String::toUpperCase, new EnumValidator<>(SupportTicket.Category.class), ValidatingType.invalid, controller::getTicketsByCategory)
                .master("master.support.ticket");

        menuBuilder.construct("master.support.ticket", true);
    }

    // change this grace code when the actually controller comes in
    public void supportTrade(TestTradeController controller){
        menuBuilder.option(Trade.class, OperationType.add, 3)
                .input("Initiator", controller::ifTradeNotExist, ValidatingType.exists)
                .input("Respondent", String::toUpperCase, null, ValidatingType.invalid)
                .input("Lending Item", String::toUpperCase, null, ValidatingType.invalid)
                .input("Borrowing Item", String::toUpperCase, null, ValidatingType.invalid)
                .input("Permanency", String::toUpperCase, null, ValidatingType.invalid)
                .input("Date", String::toUpperCase, null, ValidatingType.invalid)
                .input("Location", String::toUpperCase, null, ValidatingType.invalid)
                .submit("confirm", controller::addTrade)
                .master("master.support.trade");

        menuBuilder.option(Trade.class, OperationType.edit, 4)
                .input("Initiator", controller::ifTradeNotExist, ValidatingType.exists)
                .input("New Date/ Time", String::toUpperCase, null, ValidatingType.invalid)
                .submit("confirm", controller::testEditDateAndTime)
                .master("master.support.trade");

        menuBuilder.option(Trade.class, OperationType.edit, 5)
                .input("Initiator", controller::ifTradeNotExist, ValidatingType.exists)
                .input("New Location", String::toUpperCase, null, ValidatingType.invalid)
                .submit("confirm", controller::addTrade)
                .master("master.support.trade");

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
