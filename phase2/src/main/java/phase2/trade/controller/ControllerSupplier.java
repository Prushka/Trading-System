package phase2.trade.controller;

import phase2.trade.controller.ControllerResources;

@FunctionalInterface
public interface ControllerSupplier<T> {

    T get(ControllerResources controllerResources);

}
