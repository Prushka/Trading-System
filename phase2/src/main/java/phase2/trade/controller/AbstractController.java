package phase2.trade.controller;

import javafx.scene.Node;
import javafx.stage.Stage;
import phase2.trade.command.CommandFactory;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.presenter.PopupFactory;
import phase2.trade.presenter.SceneManager;
import phase2.trade.user.AccountManager;
import phase2.trade.view.SceneFactory;

public abstract class AbstractController {

    private final SceneManager sceneManager;

    public AbstractController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    protected AccountManager getAccountManager() {
        return sceneManager.getAccountManager();
    }

    protected PopupFactory getPopupFactory() {
        return sceneManager.getPopupFactory();
    }

    protected CommandFactory getCommandFactory() {
        return sceneManager.getCommandFactory();
    }
}
