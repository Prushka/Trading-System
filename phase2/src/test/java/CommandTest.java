import org.hibernate.cfg.Configuration;
import org.junit.Test;
import phase2.trade.command.AddItemToItemList;
import phase2.trade.command.Command;
import phase2.trade.gateway.Callback;
import phase2.trade.gateway.UserGateway;
import phase2.trade.gateway.database.DatabaseResourceBundle;
import phase2.trade.gateway.database.DatabaseResourceBundleImpl;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.user.PersonalUser;

import java.util.List;

import static org.junit.Assert.*;

import java.util.logging.Level;

public class CommandTest {

    private final DatabaseResourceBundle bundle;

    private final UserGateway userGateway;

    public CommandTest() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        bundle = new DatabaseResourceBundleImpl();
        userGateway = bundle.getUserGateway();
    }

    private void save() {

        userGateway.submitSession(() -> userGateway.add(personalUser));
    }

    PersonalUser personalUser = new PersonalUser("name", "email", "password", "country", "city");

    @Test
    public void testCommand() {
        save();
        Command<Item> addItem = new AddItemToItemList(bundle, personalUser, InventoryType.INVENTORY);

        addItem.execute(null, "testName", "testDescription");
    }

    @Test
    public void testUndo() {
        testCommand();
        final Command<?> command;
        bundle.getCommandGateway().openCurrentSession();
        command = bundle.getCommandGateway().findById(1L);
        bundle.getCommandGateway().closeCurrentSession();

        command.setGatewayBundle(bundle);
        command.isUndoable(result -> assertEquals(0, result.size()));
        command.undo();
    }
}
