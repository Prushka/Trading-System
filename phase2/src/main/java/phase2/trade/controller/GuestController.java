package phase2.trade.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import phase2.trade.database.GatewayBundle;
import phase2.trade.item.ItemManager;
import phase2.trade.user.AccountManager;

import java.net.URL;
import java.util.ResourceBundle;

public class GuestController extends AbstractController implements Initializable {

    // how to access overall database inventory?
    // private final ItemManager itemManager;
    // private final AccountManager accountManager;

    public Label submissionResult;

    private StringProperty submissionResultProperty;

    public GuestController(GatewayBundle gatewayBundle) {
        super(gatewayBundle);
    }

    /*
    public GuestController(GatewayBundle gatewayBundle, ItemManager itemManager) {
        super(gatewayBundle);
        this.itemManager = itemManager;
    }
     */

    public void browseItemsByCategory(ActionEvent actionEvent) {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submissionResultProperty = new SimpleStringProperty("");
        submissionResult.textProperty().bind(submissionResultProperty);
    }
}
