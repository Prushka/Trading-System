package phase2.trade.controller;

public class ControllerFactory {

    private final ControllerResources controllerResources;

    public ControllerFactory(ControllerResources controllerResources) {
        this.controllerResources = controllerResources;
    }

    public <T> T getController(ControllerSupplier<T> supplier) {
        return supplier.get(controllerResources);
    }

    public String getViewFileFromController(Object controllerClass) {
        if(!controllerClass.getClass().isAnnotationPresent(ControllerProperty.class)){
            return null;
        }
        return controllerClass.getClass().getAnnotation(ControllerProperty.class).viewFile();
    }
}
