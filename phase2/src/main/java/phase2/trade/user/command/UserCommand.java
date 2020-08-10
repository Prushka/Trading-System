package phase2.trade.user.command;

import phase2.trade.command.Command;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.UserGateway;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
public abstract class UserCommand<T extends User> extends Command<T> {

    public UserCommand(GatewayBundle gatewayBundle, User operator) {
        super(gatewayBundle, operator);
    }

    public UserCommand() {}

    public UserCommand(GatewayBundle gatewayBundle) {
        super(gatewayBundle);
    }

    protected UserGateway getUserGateway() {
        return getEntityBundle().getUserGateway();
    }
}
