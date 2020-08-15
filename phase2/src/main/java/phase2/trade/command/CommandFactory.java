package phase2.trade.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;
import phase2.trade.user.SystemUser;
import phase2.trade.user.User;
import phase2.trade.user.UserFactory;

import java.util.function.Consumer;

public class CommandFactory {

    private static final Logger logger = LogManager.getLogger(CommandFactory.class);

    private final GatewayBundle gatewayBundle;

    private final AccountManager accountManager;

    public CommandFactory(GatewayBundle gatewayBundle, AccountManager accountManager) {
        this.gatewayBundle = gatewayBundle;
        this.accountManager = accountManager;
    }

    public <T extends Command<?>> T getCommand(CommandSupplier<T> commandSupplier, Consumer<T> commandConsumer, boolean useSystem) {
        User operator = useSystem ? new UserFactory(gatewayBundle.getConfigBundle().getPermissionConfig()).configureSystemUser() : accountManager.getLoggedInUser();
        T command = commandSupplier.get();
        if (operator == null) {
            logger.error("The Command: " + command.getClass().getSimpleName() + " was created not using System as a user and there is no currently logged in user!");
        }
        command.injectByFactory(gatewayBundle, operator);
        commandConsumer.accept(command);
        return command;
    }

    public <T extends Command<?>> T getCommand(CommandSupplier<T> commandSupplier, Consumer<T> commandConsumer) {
        return this.getCommand(commandSupplier, commandConsumer, false);
    }

    public <T extends Command<?>> T getCommand(CommandSupplier<T> commandSupplier, boolean useSystem) {
        return this.getCommand(commandSupplier, t -> {
        }, useSystem);
    }


    public <T extends Command<?>> T getCommand(CommandSupplier<T> commandSupplier) {
        return this.getCommand(commandSupplier, false);
    }
}
