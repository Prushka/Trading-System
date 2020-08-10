package phase2.trade.controller;

import javafx.scene.Node;
import javafx.stage.Stage;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.presenter.PopupFactory;
import phase2.trade.presenter.SceneSwitcher;
import phase2.trade.view.SceneFactory;

public abstract class AbstractController {

    private final SceneFactory sceneFactory = new SceneFactory();

    private PopupFactory popupFactory;

    private SceneSwitcher sceneSwitcher;

    protected final GatewayBundle gatewayBundle;

    protected Stage window;

    public AbstractController(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
    }

    protected void initializeAbstractController(Node node) {
        // if (node == null || node.getScene() == null || node.getScene().getWindow() == null) return;
        this.initializeAbstractController((Stage) node.getScene().getWindow());
    }

    protected void initializeAbstractController(Stage window) {
        sceneSwitcher = new SceneSwitcher(window);
        popupFactory = new PopupFactory(window);
    }

    public SceneFactory getSceneFactory() {
        return sceneFactory;
    }

    public SceneSwitcher getSceneSwitcher() {
        return sceneSwitcher;
    }

    public PopupFactory getPopupFactory() {
        return popupFactory;
    }

    // There are two approaches to find the root Scene and window
    // They all require casting
    // 1. through ActionEvent
    // 2. through any nodes
    protected void setWindow(Node node) {
        setRootWindow((Stage) node.getScene().getWindow());
    }

    protected void setRootWindow(Stage rootWindow) {
        this.window = rootWindow;
    }
}
