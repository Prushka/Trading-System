package phase2.trade.config.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerResources;

import java.net.URL;
import java.util.ResourceBundle;

public class AdministrativeConfigController extends AbstractController implements Initializable {

    @FXML
    private VBox root;

    public AdministrativeConfigController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
