package phase2.trade.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.validator.ValidatorFactory;
import phase2.trade.view.SceneFactory;

import java.io.IOException;

public abstract class AbstractController {

    private final SceneFactory sceneFactory = new SceneFactory();

    private final ValidatorFactory validatorFactory = new ValidatorFactory();

    GatewayBundle gatewayBundle;

    public AbstractController(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
    }

    GatewayBundle getGatewayBundle() {
        return this.gatewayBundle;
    }

    public SceneFactory getSceneFactory() {
        return sceneFactory;
    }
}
