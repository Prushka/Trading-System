import org.hibernate.cfg.Configuration;
import org.junit.Test;
import phase2.trade.gateway.ConfigBundle;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.item.command.AlterItem;
import phase2.trade.command.CRUDType;
import phase2.trade.command.Command;
import phase2.trade.gateway.UserGateway;
import phase2.trade.gateway.database.DatabaseResourceBundle;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.user.RegularUser;

import static org.junit.Assert.*;

import java.util.logging.Level;

public class CommandTest {

    private final GatewayBundle bundle;

    private final EntityBundle entityBundle;

    private final UserGateway userGateway;

    public CommandTest() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        bundle = new GatewayBundle(new DatabaseResourceBundle().getDaoBundle(), new ConfigBundle());
        entityBundle = bundle.getEntityBundle();
        userGateway = bundle.getEntityBundle().getUserGateway();
    }

    private void save() {

        userGateway.submitSession(() -> userGateway.add(regularUser));
    }

    RegularUser regularUser = new RegularUser("name", "email", "password", "country", "city");

    @Test
    public void testCommand() {
        save();
        Command<Item> addItem = new AddItemToItemList(entityBundle, regularUser, InventoryType.INVENTORY);

        addItem.execute(null, "testName", "testDescription");

        Command<Item> alterItem = new AlterItem(entityBundle, regularUser, 1L);
        alterItem.execute(null, "testName2", "testDescription2");
    }

    @Test
    public void testUndo() {
        testCommand();
        final Command<?> command;
        entityBundle.getCommandGateway().openCurrentSession();
        command = entityBundle.getCommandGateway().findById(1L);
        entityBundle.getCommandGateway().closeCurrentSession();

        command.setEntityBundle(entityBundle);
        command.isUndoable(result -> {
            assertEquals(1, result.size());
            assertEquals(result.get(0).getCRUDType(), CRUDType.UPDATE);
        });
        // command.undo();
    }
}
