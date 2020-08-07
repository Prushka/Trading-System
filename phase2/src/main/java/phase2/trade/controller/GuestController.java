package phase2.trade.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.item.Category;
import phase2.trade.validator.ValidatorBind;
import phase2.trade.validator.ValidatorType;

import java.net.URL;
import java.util.ResourceBundle;

public class GuestController extends AbstractController implements Initializable {



    private StringProperty submissionResultProperty;

    public GuestController(GatewayBundle gatewayBundle) {
        super(gatewayBundle);

    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
