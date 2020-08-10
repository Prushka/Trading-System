package phase2.trade.controller;

import javafx.scene.Node;
import javafx.stage.Stage;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.presenter.PopupFactory;
import phase2.trade.presenter.SceneManager;
import phase2.trade.user.AccountManager;
import phase2.trade.view.SceneFactory;

public abstract class AbstractController {

    private PopupFactory popupFactory;

    private SceneManager sceneManager;

    public AbstractController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        initializeAbstractController(sceneManager.getWindow());
    }

    private void initializeAbstractController(Stage window) {
        popupFactory = new PopupFactory(window);
    }

    public SceneFactory getSceneFactory() {
        return sceneManager.getSceneFactory();
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public PopupFactory getPopupFactory() {
        return popupFactory;
    }

    protected AccountManager getAccountManager() {
        return sceneManager.getAccountManager();
    }

    protected GatewayBundle getGatewayBundle() {
        return sceneManager.getGatewayBundle();
    }
}
