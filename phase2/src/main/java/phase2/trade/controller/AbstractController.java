package phase2.trade.controller;

import phase2.trade.gateway.GatewayBundle;
import phase2.trade.validator.ValidatorFactory;
import phase2.trade.view.SceneFactory;

public abstract class AbstractController {

    private final SceneFactory sceneFactory = new SceneFactory();

    private final ValidatorFactory validatorFactory = new ValidatorFactory();

    protected final GatewayBundle gatewayBundle;

    public AbstractController(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
    }

    public SceneFactory getSceneFactory() {
        return sceneFactory;
    }
}
