package phase2.trade.controller;

import javafx.scene.Node;
import javafx.stage.Stage;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.presenter.PopupFactory;
import phase2.trade.presenter.SceneManager;
import phase2.trade.user.AccountManager;
import phase2.trade.view.SceneFactory;

public abstract class AbstractController {

    private SceneManager sceneManager;

    public AbstractController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    protected AccountManager getAccountManager() {
        return sceneManager.getAccountManager();
    }

    protected GatewayBundle getGatewayBundle() {
        return sceneManager.getGatewayBundle();
    }

    protected PopupFactory getPopupFactory() {
        return sceneManager.getPopupFactory();
    }
}
