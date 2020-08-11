package phase2.trade.presenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import phase2.trade.controller.ControllerResources;
import phase2.trade.view.SceneFactory;

import java.io.IOException;

public class SceneManager {

    private final Stage window;
    private final SceneFactory sceneFactory;
    private final ControllerResources controllerResources;

    public SceneManager(ControllerResources controllerResources) {
        this.controllerResources = controllerResources;
        this.window = controllerResources.getWindow();
        sceneFactory = new SceneFactory(controllerResources);
    }

    public void switchScene(String fileName, Object controller, boolean applyCSS) {
        Scene scene = new Scene(sceneFactory.loadPane(fileName, controller));
        if (applyCSS) {
            scene.getStylesheets().add("css/trade.css");
        }
        window.setScene(scene);
    }

    public void switchScene(String fileName, Object controller) {
        this.switchScene(fileName, controller, true);
    }

    public <T> T switchScene(String fileName, ControllerSupplier<T> controller) {
        T instantiated = controller.get(controllerResources);
        this.switchScene(fileName, controller.get(controllerResources), true);
        return instantiated;
    }

    public Stage getWindow() {
        return window;
    }

    public <T> T addPane(String fileName, ControllerSupplier<T> controller, Pane pane) {
        T instantiated = controller.get(controllerResources);
        pane.getChildren().addAll(sceneFactory.loadPane(fileName, instantiated));
        return instantiated;
    }

    public void addPane(String fileName, Object controller, Pane pane) {
        pane.getChildren().addAll(sceneFactory.loadPane(fileName, controller));
    }


    public Parent loadPane(String fileName, Object controller) {
        return sceneFactory.loadPane(fileName, controller);
    }

    public <T> Parent loadPane(String fileName, ControllerSupplier<T> controller) {
        return sceneFactory.loadPane(fileName, controller);
    }

}
