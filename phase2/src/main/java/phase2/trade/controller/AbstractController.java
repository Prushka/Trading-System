package phase2.trade.controller;

import javafx.stage.Stage;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.validator.ValidatorFactory;
import phase2.trade.view.SceneFactory;

public abstract class AbstractController {

    private final SceneFactory sceneFactory = new SceneFactory();

    private final ValidatorFactory validatorFactory = new ValidatorFactory();

    protected final GatewayBundle gatewayBundle;

    protected Stage parentWindow;

    public AbstractController(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
    }

    public AbstractController(GatewayBundle gatewayBundle, Stage parentWindow) {
        // some controllers are instantiated within other controllers, which means they need a reference to the current scene / window
        this.gatewayBundle = gatewayBundle;
        this.parentWindow = parentWindow;
    }

    public SceneFactory getSceneFactory() {
        return sceneFactory;
    }
}
