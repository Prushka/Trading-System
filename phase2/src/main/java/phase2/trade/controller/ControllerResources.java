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

/**
 * The Controller resources, a Facade class that contains multiple fields.<p>
 * The object of this class is required in every controller since they need to access Gateways (as {@link GatewayBundle} for {@link CommandFactory})<p>
 * with configured thread pool and connection pool, <p>
 * which they won't be able to configure on their own. Similarly, they have to access the {@link AccountManager}, {@link SceneManager} etc.
 *
 * @author Dan Lyu
 */
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

    /**
     * Constructs a new Controller resources.
     *
     * @param gatewayBundle  the gateway bundle
     * @param shutdownHook   the shutdown hook
     * @param window         the window
     * @param accountManager the account manager
     */
    public ControllerResources(GatewayBundle gatewayBundle, ShutdownHook shutdownHook, Stage window, AccountManager accountManager) {
        this.gatewayBundle = gatewayBundle;
        this.window = window;
        this.accountManager = accountManager;
        notificationFactory = new PopupFactory(window);
        commandFactory = new CommandFactory(gatewayBundle, accountManager);
        sceneManager = new SceneManager(this);
        reReReRe = new ReReReRe(gatewayBundle.getConfigBundle(), shutdownHook);
    }

    /**
     * Gets window.
     *
     * @return the window
     */
    public Stage getWindow() {
        return window;
    }

    /**
     * Gets account manager.
     *
     * @return the account manager
     */
    public AccountManager getAccountManager() {
        return accountManager;
    }

    /**
     * Gets gateway bundle.
     *
     * @return the gateway bundle
     */
    protected GatewayBundle getGatewayBundle() {
        return gatewayBundle;
    }

    /**
     * Gets notification factory.
     *
     * @return the notification factory
     */
    protected NotificationFactory getNotificationFactory() {
        return notificationFactory;
    }

    /**
     * Gets command factory.
     *
     * @return the command factory
     */
    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    /**
     * Gets scene manager.
     *
     * @return the scene manager
     */
    public SceneManager getSceneManager() {
        return sceneManager;
    }

    /**
     * Gets pane.
     *
     * @return the pane
     */
    protected Map<DashboardPane, Pane> getPane() {
        return panes;
    }

    /**
     * Gets re re re re.
     *
     * @return the re re re re
     */
    public ReReReRe getReReReRe() {
        return reReReRe;
    }
}
