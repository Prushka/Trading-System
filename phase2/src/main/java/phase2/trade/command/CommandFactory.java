package phase2.trade.command;

import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;
import phase2.trade.user.User;
import phase2.trade.user.UserFactory;

public class CommandFactory {

    private final GatewayBundle gatewayBundle;

    private final AccountManager accountManager;

    public CommandFactory(GatewayBundle gatewayBundle, AccountManager accountManager) {
        this.gatewayBundle = gatewayBundle;
        this.accountManager = accountManager;
    }

    public <T> T getCommand(CommandSupplier<T> commandSupplier, boolean useSystem) {
        User operator = useSystem ? new UserFactory(gatewayBundle.getConfigBundle().getPermissionConfig()).configureSystemUser() : accountManager.getLoggedInUser();
        return commandSupplier.get(gatewayBundle, operator);
    }
}
