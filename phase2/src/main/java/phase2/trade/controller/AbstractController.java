package phase2.trade.controller;

import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.command.CommandFactory;
import phase2.trade.config.ConfigBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.refresh.ReType;
import phase2.trade.refresh.Refreshable;
import phase2.trade.refresh.Reloadable;
import phase2.trade.user.AccountManager;
import phase2.trade.view.NodeFactory;
import phase2.trade.view.NotificationFactory;
import phase2.trade.view.SceneManager;

/**
 * The base Controller class for all other Controllers.
 *
 * @author Dan Lyu
 * @see ControllerProperty
 * @see ControllerResources
 */
@ControllerProperty(viewFile = "abstract_v.fxml")
public abstract class AbstractController implements Reloadable, Refreshable {

    private static final Logger logger = LogManager.getLogger(AbstractController.class);

    private final ControllerResources controllerResources;

    private final NodeFactory nodeFactory = new NodeFactory();

    private final ControllerFactory controllerFactory;

    /**
     * Constructs a new Abstract controller.
     *
     * @param controllerResources the controller resources
     */
    public AbstractController(ControllerResources controllerResources) {
        this.controllerResources = controllerResources;
        controllerResources.getReReReRe().subscribeRefreshable(this.getClass().getSimpleName(), this);
        controllerResources.getReReReRe().subscribeReloadable(this.getClass().getSimpleName(), this);
        controllerFactory = new ControllerFactory(controllerResources);
        clearToolBars();
    }

    private void clearToolBars() {
        if (getPane(DashboardPane.TOP) != null) getPane(DashboardPane.TOP).getChildren().clear();
        if (getPane(DashboardPane.LEFT) != null) getPane(DashboardPane.LEFT).getChildren().clear();
        if (getPane(DashboardPane.RIGHT) != null) getPane(DashboardPane.RIGHT).getChildren().clear();
    }

    /**
     * Publish a change using gateway remotely.<p>
     * This is used to ensure one change in one application will cause reloads in multiple concurrently running applications.<p>
     * The current implementation uses a {@link phase2.trade.gateway.GatewayPubSub} interface and is based on redis's existing pub-sub pattern.<p>
     * If those applications do not have objects that are of such classes registered as observers, those applications won't get affected or reload.
     *
     * @param affectedControllers the affected controllers
     */
    protected void publishGateway(Class<?>... affectedControllers) {
        controllerResources.getReReReRe().publishGateway(affectedControllers);
    }

    /**
     * Publish a change remotely to ask the reload of the current class.
     */
    protected void publishGateway() {
        controllerResources.getReReReRe().publishGateway(this.getClass().getSimpleName());
    }

    /**
     * Publish locally. The publisher itself will be refreshed.
     *
     * @param reType the re type
     */
    protected void publish(ReType reType) {
        controllerResources.getReReReRe().publish(reType, this.getClass().getSimpleName());
    }

    /**
     * Publish locally. The publisher determines the controllers to reload / refresh depending on the {@link ReType} (if they already subscribed) in {@link phase2.trade.refresh.ReReReRe}.
     *
     * @param reType              the re type
     * @param effectedControllers the effected controllers
     */
    protected void publish(ReType reType, Class<?>... effectedControllers) {
        controllerResources.getReReReRe().publish(reType, effectedControllers);
    }

    public void reload() {
        logger.info("Reloading: " + this.getClass().getSimpleName());
    }

    public void refresh() {
        logger.info("Refreshing: " + this.getClass().getSimpleName());
    }

    /**
     * Gets scene manager.
     *
     * @return the scene manager
     */
    protected SceneManager getSceneManager() {
        return controllerResources.getSceneManager();
    }

    /**
     * Gets controller resources.
     *
     * @return the controller resources
     */
    protected ControllerResources getControllerResources() {
        return controllerResources;
    }

    /**
     * Gets account manager.
     *
     * @return the account manager
     */
    protected AccountManager getAccountManager() {
        return controllerResources.getAccountManager();
    }

    /**
     * Gets notification factory.
     *
     * @return the notification factory
     */
    protected NotificationFactory getNotificationFactory() {
        return controllerResources.getNotificationFactory();
    }

    /**
     * Gets command factory.
     *
     * @return the command factory
     */
    protected CommandFactory getCommandFactory() {
        return controllerResources.getCommandFactory();
    }

    /**
     * Gets gateway bundle.
     *
     * @return the gateway bundle
     */
    protected GatewayBundle getGatewayBundle() {
        return controllerResources.getGatewayBundle();
    }

    /**
     * Gets pane.
     *
     * @param name the name
     * @return the pane
     */
    protected Pane getPane(DashboardPane name) {
        return controllerResources.getPane().get(name);
    }

    /**
     * Put pane.
     *
     * @param name the name
     * @param pane the pane
     */
    protected void putPane(DashboardPane name, Pane pane) {
        controllerResources.getPane().put(name, pane);
    }

    /**
     * Gets node factory.
     *
     * @return the node factory
     */
    protected NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    /**
     * Gets config bundle.
     *
     * @return the config bundle
     */
    protected ConfigBundle getConfigBundle() {
        return controllerResources.getGatewayBundle().getConfigBundle();
    }

    /**
     * Gets controller factory.
     *
     * @return the controller factory
     */
    public ControllerFactory getControllerFactory() {
        return controllerFactory;
    }
}
