package phase2.trade.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;
import phase2.trade.user.SystemUser;
import phase2.trade.user.User;
import phase2.trade.user.UserFactory;

import java.util.function.Consumer;

/**
 * The Command factory that construct Commands using {@link CommandSupplier}.<p>
 * It also injects the dependencies {@link GatewayBundle} and {@link User} into Command before returning it.
 *
 * @author Dan Lyu
 */
public class CommandFactory {

    private static final Logger logger = LogManager.getLogger(CommandFactory.class);

    private final GatewayBundle gatewayBundle;

    private final AccountManager accountManager;

    /**
     * Constructs a new Command factory.
     *
     * @param gatewayBundle  the gateway bundle
     * @param accountManager the account manager
     */
    public CommandFactory(GatewayBundle gatewayBundle, AccountManager accountManager) {
        this.gatewayBundle = gatewayBundle;
        this.accountManager = accountManager;
    }

    /**
     * Gets command.
     *
     * @param <T>             the type parameter of the Command
     * @param commandSupplier the command supplier
     * @param commandConsumer the command consumer, used to set any fields this Command requires
     * @param useSystem       if set to <code>true</code>, a {@link SystemUser} will be used as the operator of that Command
     * @return the command
     */
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

    /**
     * Gets command, the current logged in {@link User} will be the default operator for that Command.
     *
     * @param <T>             the type parameter of the Command
     * @param commandSupplier the command supplier
     * @param commandConsumer the command consumer, used to set any fields this Command requires
     * @return the command
     */
    public <T extends Command<?>> T getCommand(CommandSupplier<T> commandSupplier, Consumer<T> commandConsumer) {
        return this.getCommand(commandSupplier, commandConsumer, false);
    }

    /**
     * Gets command.
     *
     * @param <T>             the type parameter
     * @param commandSupplier the command supplier
     * @param useSystem       the use system
     * @return the command
     */
    public <T extends Command<?>> T getCommand(CommandSupplier<T> commandSupplier, boolean useSystem) {
        return this.getCommand(commandSupplier, t -> {
        }, useSystem);
    }


    /**
     * Gets command.
     *
     * @param <T>             the type parameter
     * @param commandSupplier the command supplier
     * @return the command
     */
    public <T extends Command<?>> T getCommand(CommandSupplier<T> commandSupplier) {
        return this.getCommand(commandSupplier, false);
    }
}
