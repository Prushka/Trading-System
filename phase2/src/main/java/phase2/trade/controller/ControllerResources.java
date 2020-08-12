package phase2.trade.controller;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import phase2.trade.command.CommandFactory;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.presenter.PopupFactory;
import phase2.trade.view.SceneManager;
import phase2.trade.user.AccountManager;

import java.util.Map;

public class ControllerResources {

    private final Stage window;

    private final PopupFactory popupFactory;

    private final GatewayBundle gatewayBundle;

    private final AccountManager accountManager;

    private final CommandFactory commandFactory;

    private final SceneManager sceneManager;

    private Map<String, Pane> panes;

    public ControllerResources(GatewayBundle gatewayBundle, Stage window, AccountManager accountManager) {
        this.gatewayBundle = gatewayBundle;
        this.window = window;
        this.accountManager = accountManager;
        popupFactory = new PopupFactory(window);
        commandFactory = new CommandFactory(gatewayBundle, accountManager);
        sceneManager = new SceneManager(this);
    }

    public Stage getWindow() {
        return window;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    protected GatewayBundle getGatewayBundle() {
        return gatewayBundle;
    }

    protected PopupFactory getPopupFactory() {
        return popupFactory;
    }

    protected CommandFactory getCommandFactory() {
        return commandFactory;
    }

    protected SceneManager getSceneManager() {
        return sceneManager;
    }

    protected Map<String, Pane> getPanes() {
        return panes;
    }
}
