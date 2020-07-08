package group.system;

import group.menu.Menu;
import group.menu.MenuFactory;
import group.menu.MenuFactory.OperationType;
import group.menu.MenuFactory.ValidatingType;
import group.menu.node.MasterOptionNode;
import group.menu.validator.EnumValidator;
import group.notification.SupportTicket;

public class MenuConstructor {

    private final Menu menu;

    public MenuConstructor(TestController controller) {
        MenuFactory menuFactory = new MenuFactory();
        menuFactory.option(SupportTicket.class, OperationType.add, 1)
                .input("content", controller::ifTicketContentNotExist, ValidatingType.exists)
                .input("category", String::toUpperCase, new EnumValidator<>(SupportTicket.Category.class), ValidatingType.invalid)
                .input("priority", String::toUpperCase, new EnumValidator<>(SupportTicket.Priority.class), ValidatingType.invalid)
                .submit("confirm", controller::addTicket)
                .master("master.support.ticket");

        menuFactory.option(SupportTicket.class, OperationType.query, 2)
                .submit("category", String::toUpperCase, new EnumValidator<>(SupportTicket.Category.class), ValidatingType.invalid, controller::getTicketsByCategory)
                .master("master.support.ticket");

        MasterOptionNode entryNode = menuFactory.construct("master.support.ticket");
        menuFactory.constructFinal();
        menu = new Menu(entryNode);

        ConsoleSystem console = new ConsoleSystem();
        console.run(getMenu());
    }

    public Menu getMenu() {
        return menu;
    }
}
