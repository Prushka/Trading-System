package phase2.trade.controller;

import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.command.CommandFactory;
import phase2.trade.config.ConfigBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.view.NodeFactory;
import phase2.trade.view.PopupFactory;
import phase2.trade.view.SceneManager;
import phase2.trade.user.AccountManager;

import java.util.HashMap;
import java.util.Map;

@ControllerProperty(viewFile = "abstract_v.fxml")
public abstract class AbstractController implements Reloadable {

    private static final Logger logger = LogManager.getLogger(AbstractController.class);

    private final ControllerResources controllerResources;

    private final NodeFactory nodeFactory = new NodeFactory();

    private final Map<String, String> valueLanguage = new HashMap<>();

    public AbstractController(ControllerResources controllerResources) {
        this.controllerResources = controllerResources;
        controllerResources.registerReloadable(this.getClass().getSimpleName(), this);
        if (getPane("topBar") != null) getPane("topBar").getChildren().clear();
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

    protected void publish() {
        controllerResources.publish(this.getClass().getSimpleName());
    }

    protected void publish(Class<?>... effectedControllers) {
        StringBuilder simpleRepresentation = new StringBuilder();
        for (Class<?> clazz : effectedControllers) {
            simpleRepresentation.append(clazz.getSimpleName()).append(",");
        }
        controllerResources.publish(simpleRepresentation.toString());
    }

    // Reload: Reload data from gateway if applicable
    public void reload() {
        logger.info("Reloading: " + this.getClass().getSimpleName());
    }

    // Refresh: If the view has to be refreshed (Observable doesn't apply). No gateway involved
    protected void refresh() {
        logger.info("Reloading: " + this.getClass().getSimpleName());
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

    protected PopupFactory getPopupFactory() {
        return controllerResources.getPopupFactory();
    }

    protected CommandFactory getCommandFactory() {
        return controllerResources.getCommandFactory();
    }

    protected GatewayBundle getGatewayBundle() {
        return controllerResources.getGatewayBundle();
    }

    protected Pane getPane(String name) {
        return controllerResources.getPanes().get(name);
    }

    protected void putPane(String name, Pane pane) {
        controllerResources.getPanes().put(name, pane);
    }

    protected NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    protected ConfigBundle getConfigBundle() {
        return controllerResources.getGatewayBundle().getConfigBundle();
    }
}
