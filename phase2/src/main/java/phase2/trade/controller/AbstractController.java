package phase2.trade.controller;

import phase2.trade.command.CommandFactory;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.presenter.PopupFactory;
import phase2.trade.view.SceneManager;
import phase2.trade.user.AccountManager;

public abstract class AbstractController {

    private final ControllerResources controllerResources;

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
}
