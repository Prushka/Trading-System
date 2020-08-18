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

import java.util.HashMap;
import java.util.Map;

@ControllerProperty(viewFile = "abstract_v.fxml")
public abstract class AbstractController implements Reloadable, Refreshable {

    private static final Logger logger = LogManager.getLogger(AbstractController.class);

    private final ControllerResources controllerResources;

    private final NodeFactory nodeFactory = new NodeFactory();

    private final ControllerFactory controllerFactory;

    private final Map<String, String> valueLanguage = new HashMap<>();

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

    protected String getLanguageByValue(String key) {
        return valueLanguage.get(key);
    }

    protected void putLanguageValue(String key, String value) {
        valueLanguage.put(key, value);
    }

    protected String getValueByLanguage(String value) {
        for (Map.Entry<String, String> entry : valueLanguage.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    protected void publishGateway() {
        controllerResources.getReReReRe().publishGateway(this.getClass().getSimpleName());
    }

    protected void publishGateway(Class<?>... effectedControllers) {
        controllerResources.getReReReRe().publishGateway(effectedControllers);
    }

    protected void publish(ReType reType) {
        controllerResources.getReReReRe().publish(reType, this.getClass().getSimpleName());
    }

    protected void publish(ReType reType, Class<?>... effectedControllers) {
        controllerResources.getReReReRe().publish(reType, effectedControllers);
    }

    // Reload: Reload data from gateway if applicable
    public void reload() {
        logger.info("Reloading: " + this.getClass().getSimpleName());
    }

    // Refresh: If the view has to be refreshed (Observable doesn't apply). No gateway involved
    public void refresh() {
        logger.info("Refreshing: " + this.getClass().getSimpleName());
    }

    protected SceneManager getSceneManager() {
        return controllerResources.getSceneManager();
    }

    protected ControllerResources getControllerResources() {
        return controllerResources;
    }

    protected AccountManager getAccountManager() {
        return controllerResources.getAccountManager();
    }

    protected NotificationFactory getNotificationFactory() {
        return controllerResources.getNotificationFactory();
    }

    protected CommandFactory getCommandFactory() {
        return controllerResources.getCommandFactory();
    }

    protected GatewayBundle getGatewayBundle() {
        return controllerResources.getGatewayBundle();
    }

    protected Pane getPane(DashboardPane name) {
        return controllerResources.getPane().get(name);
    }

    protected void putPane(DashboardPane name, Pane pane) {
        controllerResources.getPane().put(name, pane);
    }

    protected NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    protected ConfigBundle getConfigBundle() {
        return controllerResources.getGatewayBundle().getConfigBundle();
    }

    public ControllerFactory getControllerFactory() {
        return controllerFactory;
    }
}
