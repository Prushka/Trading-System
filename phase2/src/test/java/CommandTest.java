import org.junit.Test;
import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.command.CommandFactory;
import phase2.trade.gateway.ConfigBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemList;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.item.command.AlterItemInInventory;
import phase2.trade.command.Command;
import phase2.trade.gateway.database.DatabaseResourceBundle;
import phase2.trade.item.Item;
import phase2.trade.item.command.GetItems;
import phase2.trade.user.AccountManager;
import phase2.trade.user.RegularUser;

import static org.junit.Assert.*;

import java.util.logging.Level;

public class CommandTest {

    private final GatewayBundle gatewayBundle;

    private final CommandFactory commandFactory;

    public CommandTest() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        ConfigBundle configBundle = new ConfigBundle();
        gatewayBundle = new GatewayBundle(new DatabaseResourceBundle(configBundle.getDatabaseConfig()).getDaoBundle(), new ConfigBundle());

        AccountManager accountManager = new AccountManager(gatewayBundle);
        commandFactory = new CommandFactory(gatewayBundle, accountManager);
    }

    private void save() {
        gatewayBundle.getEntityBundle().getUserGateway().submitSession((gateway) -> gateway.add(regularUser));
    }

    RegularUser regularUser = new RegularUser("name", "email", "password", "country", "city");

    @Test
    public void testCommand() {
        save();
        Command<Item> addItem = commandFactory.getCommand(AddItemToItemList::new);

        addItem.execute(null, "testName", "testDescription", "MOVIE");

        Command<Item> alterItem = commandFactory.getCommand(AlterItemInInventory::new, c -> {c.setItemId(1L);});
        alterItem.execute(null, "testName2", "testDescription2", "MOVIE");
    }


    @Test
    public void testItemCommands() {
        testCommand();
        Command<ItemList> getInventory = commandFactory.getCommand(GetItems::new);
        getInventory.execute(new ResultStatusCallback<ItemList>() {
            @Override
            public void call(ItemList result, ResultStatus resultStatus) {
                System.out.println(result.get(0).getName());
            }
        });
    }

    @Test
    public void testUndo() {
        testCommand();
        final Command<?> command;
        gatewayBundle.getEntityBundle().getCommandGateway().openCurrentSession();
        command = gatewayBundle.getEntityBundle().getCommandGateway().findById(1L);
        gatewayBundle.getEntityBundle().getCommandGateway().closeCurrentSession();

        command.setGatewayBundle(gatewayBundle);
        command.isUndoable((result, status) -> {
            assertEquals(1, result.size());
            // assertEquals(result.get(0).getCRUDType(), CRUDType.UPDATE);
        });
        // command.undo();
    }
}
