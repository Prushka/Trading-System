package phase2.trade.presenter;

import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;

@FunctionalInterface
public interface ControllerSupplier<T> {

    T get(GatewayBundle gatewayBundle, SceneManager sceneManager, AccountManager accountManager);

    // This account manager is injected everywhere since it holds a reference to the current logged in user,
    // and I suppose we shouldn't pass that User around since it's an entity
    // And it's also responsible for logout and some 'account' related operations and helper methods
    // Please don't put anything else into this AccountManager, it's only supposed to handle login / register / logout
}
