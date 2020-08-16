import phase2.trade.command.CommandFactory;
import phase2.trade.config.ConfigBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.database.DatabaseResourceBundle;
import phase2.trade.user.AccountManager;
import phase2.trade.user.RegularUser;

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
/*
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
        getInventory.execute((result, resultStatus) -> {
        });
    }

    @Test
    public void testUndo() {
        testCommand();
        final Command<?> command;
        gatewayBundle.getEntityBundle().getCommandGateway().openCurrentSession();
        command = gatewayBundle.getEntityBundle().getCommandGateway().findById(1L);
        gatewayBundle.getEntityBundle().getCommandGateway().closeCurrentSession();

        command.undoIfUndoable((result, status) -> {
            assertEquals(1, result.size());
            // assertEquals(result.get(0).getCRUDType(), CRUDType.UPDATE);
        },gatewayBundle);
        // command.undoUnchecked();
    }
    */
}
