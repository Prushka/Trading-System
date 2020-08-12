package phase2.trade.controller;

import phase2.trade.presenter.ControllerSupplier;

import java.util.function.BiConsumer;

public class ControllerFactory {

    private final ControllerResources controllerResources;

    public ControllerFactory(ControllerResources controllerResources) {
        this.controllerResources = controllerResources;
    }

    public <T> void consume(ControllerSupplier<T> supplier, BiConsumer<Object, String> biConsumer) {
        T controller = getController(supplier);
        biConsumer.accept(controller, getViewFileFromController(controller));
    }

    public <T> T getController(ControllerSupplier<T> supplier) {
        return supplier.get(controllerResources);
    }

    public String getViewFileFromController(Object controllerClass) {
        return controllerClass.getClass().getAnnotation(ControllerProperty.class).viewFile();
    }
}
