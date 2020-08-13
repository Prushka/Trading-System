package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;
import phase2.trade.view.window.TextFieldAlert;

import java.net.URL;
import java.util.ResourceBundle;

public class UserInfoController extends AbstractController implements Initializable {

    @FXML
    private GridPane root;

    public UserInfoController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label userId = new Label("User Id: " + getAccountManager().getLoggedInUser().getUid());
        Label userName = new Label("User Name: " + getAccountManager().getLoggedInUser().getName());
        Label email = new Label("Email: " + getAccountManager().getLoggedInUser().getEmail());
        Label permissionGroup = new Label("PermissionGroup: " + getAccountManager().getLoggedInUser().getPermissionGroup());
        // address book + if user didnt input address
        // home = new Label("Location: " + user.getAddressBook().getSelectedAddress().getCity() + ", " + user.getAddress().getCountry());
        Label bio = new Label("Bio: ");
        Label currentStatus = new Label("Current Status: " + getAccountManager().getLoggedInUser().getUid());

        JFXButton changePassword = new JFXButton("Change Password");

        JFXTextField oldPassword = new JFXTextField();

        root.getChildren().addAll(changePassword);

        TextFieldAlert textFieldAlert = getPopupFactory().textFieldAlert("Change Password","");
        textFieldAlert.addTextField(oldPassword);

        changePassword.setOnAction(event -> textFieldAlert.display());

    }
}
