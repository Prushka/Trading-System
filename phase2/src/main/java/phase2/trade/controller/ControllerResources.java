package phase2.trade.controller;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import phase2.trade.ShutdownHook;
import phase2.trade.command.CommandFactory;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.refresh.ReReReRe;
import phase2.trade.user.AccountManager;
import phase2.trade.view.NotificationFactory;
import phase2.trade.view.PopupFactory;
import phase2.trade.view.SceneManager;

import java.util.HashMap;
import java.util.Map;

public class ControllerResources {

    private final Stage window;

    private final NotificationFactory notificationFactory;

    private final GatewayBundle gatewayBundle;

    // This account manager is injected everywhere since it holds a reference to the current logged in user,
    // and I suppose we shouldn't pass that User around since it's an entity
    // And it's also responsible for logout and some 'account' related operations and helper methods
    // Please don't put anything else into this AccountManager, it's only supposed to handle login / register / logout
    private final AccountManager accountManager;

    private final CommandFactory commandFactory;

    private final SceneManager sceneManager;

    private final Map<DashboardPane, Pane> panes = new HashMap<>();

    private final ReReReRe reReReRe;

    public ControllerResources(GatewayBundle gatewayBundle, ShutdownHook shutdownHook, Stage window, AccountManager accountManager) {
        this.gatewayBundle = gatewayBundle;
        this.window = window;
        this.accountManager = accountManager;
        notificationFactory = new PopupFactory(window);
        commandFactory = new CommandFactory(gatewayBundle, accountManager);
        sceneManager = new SceneManager(this);
        reReReRe = new ReReReRe(gatewayBundle.getConfigBundle(), shutdownHook);
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

    protected NotificationFactory getPopupFactory() {
        return notificationFactory;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    protected Map<DashboardPane, Pane> getPane() {
        return panes;
    }

    public ReReReRe getReReReRe() {
        return reReReRe;
    }
}
