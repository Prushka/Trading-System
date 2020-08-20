package phase2.trade.config.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
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
        root.setSpacing(30);
        HBox h1 = new HBox(10);

        HBox h2 = new HBox(10);


        Label label1 = new Label("Trade Limit Per Week");
        Label label2 = new Label("Trade Edits Limit");

        Slider perWeekLimit = getNodeFactory().getSlider(0, 10, getConfigBundle().getTradeConfig().getTradePerWeek());
        Slider editsLimit = getNodeFactory().getSlider(0, 10, getConfigBundle().getTradeConfig().getEditLimit());

        editsLimit.valueProperty().bindBidirectional(getConfigBundle().getTradeConfig().editLimitProperty());
        perWeekLimit.valueProperty().bindBidirectional(getConfigBundle().getTradeConfig().tradePerWeekProperty());
        h1.getChildren().addAll(label1, perWeekLimit);
        h2.getChildren().addAll(label2, editsLimit);

        root.getChildren().addAll(h1, h2);
        root.getStyleClass().addAll("settings");
    }
}
