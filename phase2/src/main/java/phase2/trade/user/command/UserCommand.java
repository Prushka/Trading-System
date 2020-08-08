package phase2.trade.user.command;

import phase2.trade.command.CRUDType;
import phase2.trade.command.Command;
import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.UserGateway;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.User;

public abstract class UserCommand<T extends User> extends Command<T> {

    public UserCommand(GatewayBundle gatewayBundle, User operator) {
        super(gatewayBundle, operator);
    }

    public UserCommand() {

    }

    protected UserGateway getUserGateway() {
        return gatewayBundle.getUserGateway();
    }
}