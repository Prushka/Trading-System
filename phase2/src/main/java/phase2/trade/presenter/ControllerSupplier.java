package phase2.trade.presenter;

import phase2.trade.controller.ControllerResources;

@FunctionalInterface
public interface ControllerSupplier<T> {

    T get(ControllerResources controllerResources);

    // This account manager is injected everywhere since it holds a reference to the current logged in user,
    // and I suppose we shouldn't pass that User around since it's an entity
    // And it's also responsible for logout and some 'account' related operations and helper methods
    // Please don't put anything else into this AccountManager, it's only supposed to handle login / register / logout
}
