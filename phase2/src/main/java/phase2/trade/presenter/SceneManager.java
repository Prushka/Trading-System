package phase2.trade.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;
import phase2.trade.view.SceneFactory;

import java.io.IOException;
import java.util.function.Supplier;

public class SceneManager {

    private final Stage window;

    private final SceneFactory sceneFactory = new SceneFactory(this);

    private final GatewayBundle gatewayBundle;

    private final AccountManager accountManager;

    public SceneManager(GatewayBundle gatewayBundle, Stage window, AccountManager accountManager) {
        this.gatewayBundle = gatewayBundle;
        this.window = window;
        this.accountManager = accountManager;
    }

    public void switchScene(String fileName, Object controller, boolean applyCSS) {
        FXMLLoader loader = sceneFactory.getLoader(fileName);
        loader.setController(controller);
        try {
            Scene scene = new Scene(loader.load());
            if (applyCSS) {
                scene.getStylesheets().add("css/trade.css");
            }
            window.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchScene(String fileName, Object controller) {
        this.switchScene(fileName, controller, true);
    }

    public <T> T switchScene(String fileName, ControllerSupplier<T> controller) {
        T instantiated = controller.get(this);
        this.switchScene(fileName, controller.get(this), true);
        return instantiated;
    }

    public Stage getWindow() {
        return window;
    }

    public <T> T addPane(String fileName, ControllerSupplier<T> controller, Pane pane) {
        T instantiated = controller.get(this);
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

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public GatewayBundle getGatewayBundle() {
        return gatewayBundle;
    }
}
