package phase2.trade.controller;

/**
 * The Controller factory.
 * It uses a {@link ControllerSupplier} to instantiate Controllers.
 * The {@link ControllerResources} will be injected.
 *
 * @author Dan Lyu
 * @see ControllerSupplier
 * @see ControllerProperty
 */
public class ControllerFactory {

    private final ControllerResources controllerResources;

    /**
     * Constructs a new Controller factory.
     *
     * @param controllerResources the controller resources
     */
    public ControllerFactory(ControllerResources controllerResources) {
        this.controllerResources = controllerResources;
    }

    /**
     * Gets controller.
     *
     * @param <T>      the type parameter
     * @param supplier the supplier
     * @return the controller
     */
    public <T> T getController(ControllerSupplier<T> supplier) {
        return supplier.get(controllerResources);
    }

    /**
     * Gets view file from controller's {@link ControllerProperty} annotation.
     *
     * @param controllerClass the controller class
     * @return the view file from controller
     */
    public String getViewFileFromController(Object controllerClass) {
        if (!controllerClass.getClass().isAnnotationPresent(ControllerProperty.class)) {
            return null;
        }
        return controllerClass.getClass().getAnnotation(ControllerProperty.class).viewFile();
    }
}
