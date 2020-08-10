package phase2.trade.command;

import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.User;

@FunctionalInterface
public interface CommandSupplier<T> {

    T get(GatewayBundle gatewayBundle, User operator);
}
