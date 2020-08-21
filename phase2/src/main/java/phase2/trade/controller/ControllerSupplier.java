package phase2.trade.controller;

/**
 * The functional interface Controller supplier.
 *
 * @param <T> the type parameter
 * @author Dan Lyu
 */
@FunctionalInterface
public interface ControllerSupplier<T> {

    /**
     * Gets Controller and injects {@link ControllerResources}.
     *
     * @param controllerResources the controller resources
     * @return the Controller
     */
    T get(ControllerResources controllerResources);

}
