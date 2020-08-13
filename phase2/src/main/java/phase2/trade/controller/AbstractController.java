package phase2.trade.controller;

import javafx.scene.layout.Pane;
import phase2.trade.command.CommandFactory;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.view.NodeFactory;
import phase2.trade.view.PopupFactory;
import phase2.trade.view.SceneManager;
import phase2.trade.user.AccountManager;

@ControllerProperty(viewFile = "abstract.fxml")
public abstract class AbstractController {

    private final ControllerResources controllerResources;

    private final NodeFactory nodeFactory = new NodeFactory();

    public AbstractController(ControllerResources controllerResources) {
        this.controllerResources = controllerResources;
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

    protected void putPane(String name, Pane pane){
        controllerResources.getPanes().put(name, pane);
    }

    protected NodeFactory getNodeFactory(){
        return nodeFactory;
    }
}
