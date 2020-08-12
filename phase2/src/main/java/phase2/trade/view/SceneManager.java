package phase2.trade.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import phase2.trade.controller.ControllerFactory;
import phase2.trade.controller.ControllerResources;
import phase2.trade.presenter.ControllerSupplier;

public class SceneManager {

    private final Stage window;
    private final ParentLoader parentLoader;
    private final ControllerResources controllerResources;

    private final ControllerFactory controllerFactory;

    public SceneManager(ControllerResources controllerResources) {
        this.controllerResources = controllerResources;
        this.window = controllerResources.getWindow();
        parentLoader = new ParentLoader();
        controllerFactory = new ControllerFactory(controllerResources);
    }

    /***********************
     *                      *
     * Switch Scene         *
     *                      *
     ***********************/

    public void switchScene(Parent parent, boolean applyCSS) {
        Scene scene = new Scene(parent);
        if (applyCSS) {
            scene.getStylesheets().add("css/trade.css");
        }
        window.setScene(scene);
    }

    public void switchScene(String fileName, Object controller, boolean applyCSS) {
        switchScene(parentLoader.loadParent(fileName, controller), applyCSS);
    }

    public void switchScene(String fileName, Object controller) {
        this.switchScene(fileName, controller, true);
    }

    public void switchScene(Object controller) {
        this.switchScene(controllerFactory.getViewFileFromController(controller), controller, true);
    }

    public <T> T switchScene(String fileName, ControllerSupplier<T> controllerSupplier) {
        T controller = controllerFactory.getController(controllerSupplier);
        this.switchScene(fileName, controller, true);
        return controller;
    }

    public <T> T switchScene(ControllerSupplier<T> controllerSupplier) {
        T controller = controllerFactory.getController(controllerSupplier);
        this.switchScene(controllerFactory.getViewFileFromController(controller), controller, true);
        return controller;
    }

    /***********************
     *                      *
     * Load Pane            *
     *                      *
     ***********************/

    public Parent loadPane(String fileName, Object controller) {
        return parentLoader.loadParent(fileName, controller);
    }

    public <T> Parent loadPane(String fileName, ControllerSupplier<T> controllerSupplier) {
        return parentLoader.loadParent(fileName, controllerFactory.getController(controllerSupplier));
    }

    public Parent loadPane(Object controller) {
        return loadPane(controllerFactory.getViewFileFromController(controller), controller);
    }

    public <T> Parent loadPane(ControllerSupplier<T> controllerSupplier) {
        return loadPane(controllerFactory.getController(controllerSupplier));
    }

    public Stage getWindow() {
        return window;
    }
}
