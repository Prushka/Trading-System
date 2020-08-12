package phase2.trade.presenter;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import phase2.trade.command.CommandProperty;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.view.SceneLoader;

public class SceneManager {

    private final Stage window;
    private final SceneLoader sceneLoader;
    private final ControllerResources controllerResources;

    public SceneManager(ControllerResources controllerResources) {
        this.controllerResources = controllerResources;
        this.window = controllerResources.getWindow();
        sceneLoader = new SceneLoader(controllerResources);
    }

    public void switchScene(String fileName, Object controller, boolean applyCSS) {
        Scene scene = new Scene(sceneLoader.loadPane(fileName, controller));
        if (applyCSS) {
            scene.getStylesheets().add("css/trade.css");
        }
        window.setScene(scene);
    }

    public void switchScene(String fileName, Object controller) {
        this.switchScene(fileName, controller, true);
    }

    public void switchScene(Object controller) {
        this.switchScene(getViewFileFromControllerAnnotation(controller.getClass()), controller, true);
    }

    public <T> T switchScene(String fileName, ControllerSupplier<T> controllerSupplier) {
        T controller = controllerSupplier.get(controllerResources);
        this.switchScene(fileName, controllerSupplier.get(controllerResources), true);
        return controller;
    }

    public <T> T switchScene(ControllerSupplier<T> controllerSupplier) {
        T controller = controllerSupplier.get(controllerResources);
        // decouple would lead to confusion since the reflection has to take place after the instantiation of the controller
        this.switchScene(getViewFileFromControllerAnnotation(controller.getClass()), controllerSupplier.get(controllerResources), true);
        return controller;
    }

    public Stage getWindow() {
        return window;
    }

    public void addPane(String fileName, Object controller, Pane pane) {
        pane.getChildren().addAll(sceneLoader.loadPane(fileName, controller));
    }

    public void addPane(Object controller, Pane pane) {
        addPane(getViewFileFromControllerAnnotation(controller.getClass()),controller,pane);
    }

    public <T> T addPane(String fileName, ControllerSupplier<T> controllerSupplier, Pane pane) {
        T controller = controllerSupplier.get(controllerResources);
        addPane(fileName, controller, pane);
        return controller;
    }

    public <T> T addPane(ControllerSupplier<T> controllerSupplier, Pane pane) {
        T controller = controllerSupplier.get(controllerResources);
        addPane(getViewFileFromControllerAnnotation(controller.getClass()), controller, pane);
        return controller;
    }

    public Parent loadPane(String fileName, Object controller) {
        return sceneLoader.loadPane(fileName, controller);
    }

    public <T> Parent loadPane(String fileName, ControllerSupplier<T> controllerSupplier) {
        return sceneLoader.loadPane(fileName, controllerSupplier);
    }

    public Parent loadPane(Object controller) {
        return loadPane(getViewFileFromControllerAnnotation(controller.getClass()), controller);
    }

    public <T> Parent loadPane(ControllerSupplier<T> controllerSupplier) {
        return loadPane(controllerSupplier.get(controllerResources));
    }

    private String getViewFileFromControllerAnnotation(Class<?> controllerClass) {
        return controllerClass.getAnnotation(ControllerProperty.class).viewFile();
    }
}
