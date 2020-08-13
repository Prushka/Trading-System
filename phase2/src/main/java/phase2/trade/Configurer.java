package phase2.trade;

import javafx.stage.Stage;
import phase2.trade.command.CommandFactory;
import phase2.trade.config.ConfigBundle;
import phase2.trade.controller.ControllerResources;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.database.DatabaseResourceBundle;
import phase2.trade.user.AccountManager;

// Use this in TradeApplication or Test
public class Configurer {

    private ControllerResources controllerResources;

    private final GatewayBundle gatewayBundle;

    private final ShutdownHook shutdownHook;

    private final CommandFactory commandFactory;

    private final AccountManager accountManager;

    public Configurer() {
        shutdownHook = new ShutdownHook();

        ConfigBundle configBundle = new ConfigBundle();
        DatabaseResourceBundle databaseResourceBundle = new DatabaseResourceBundle(configBundle.getDatabaseConfig());

        shutdownHook.addShutdownables(databaseResourceBundle, configBundle);
        gatewayBundle = new GatewayBundle(databaseResourceBundle.getDaoBundle(), configBundle);

        accountManager = new AccountManager(gatewayBundle);
        commandFactory = new CommandFactory(gatewayBundle, accountManager);
    }

    public void configure(Stage primaryStage) {
        controllerResources = new ControllerResources(gatewayBundle, primaryStage, accountManager);

        new CreatePrerequisiteIfNotExist(getControllerResources().getCommandFactory());
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public ControllerResources getControllerResources() {
        return controllerResources;
    }

    public ShutdownHook getShutdownHook() {
        return shutdownHook;
    }
}
