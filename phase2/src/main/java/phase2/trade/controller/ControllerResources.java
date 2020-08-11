package phase2.trade.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import phase2.trade.command.CommandFactory;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.presenter.ControllerSupplier;
import phase2.trade.presenter.PopupFactory;
import phase2.trade.presenter.SceneManager;
import phase2.trade.user.AccountManager;
import phase2.trade.view.SceneFactory;

import java.io.IOException;

public class ControllerResources {

    private final Stage window;

    private final PopupFactory popupFactory;

    private final GatewayBundle gatewayBundle;

    private final AccountManager accountManager;

    private final CommandFactory commandFactory;

    private final SceneManager sceneManager;

    public ControllerResources(GatewayBundle gatewayBundle, Stage window, AccountManager accountManager) {
        this.gatewayBundle = gatewayBundle;
        this.window = window;
        this.accountManager = accountManager;
        popupFactory = new PopupFactory(window);
        commandFactory = new CommandFactory(gatewayBundle, accountManager);
        sceneManager = new SceneManager(this);
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public GatewayBundle getGatewayBundle() {
        return gatewayBundle;
    }

    public PopupFactory getPopupFactory() {
        return popupFactory;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public Stage getWindow() {
        return window;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }
}
