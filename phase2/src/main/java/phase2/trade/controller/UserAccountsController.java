package phase2.trade.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class UserAccountsController extends AbstractController implements Initializable {

    public UserAccountsController(ControllerResources controllerResources){
        super(controllerResources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXButton addSubAdminButton = new JFXButton("Make a user a Sub-Admin");
        JFXButton itemRequestsButton = new JFXButton("Confirm Item Requests from a User");
        JFXButton freezeButton = new JFXButton("Freeze a User");
        JFXButton unfreezeButton = new JFXButton("Unfreeze a User");
    }
}
