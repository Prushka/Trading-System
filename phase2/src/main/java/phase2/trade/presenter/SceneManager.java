package phase2.trade.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;
import phase2.trade.view.SceneFactory;

import java.io.IOException;
import java.util.function.Supplier;

public class SceneManager {

    private final Stage window;

    private final SceneFactory sceneFactory = new SceneFactory();

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

    public <T> void switchScene(String fileName, Class<T> controllerClass, ControllerSupplier<T> controller) {
        this.switchScene(fileName, controller.get(gatewayBundle, this, accountManager), true);
    }

    public Stage getWindow() {
        return window;
    }

    public SceneFactory getSceneFactory() {
        return sceneFactory;
    }
}
