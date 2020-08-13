package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;

import java.net.URL;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "user_info.fxml")
public class UserInfoController extends AbstractController implements Initializable {

    public Label userId = new Label();
    public Label userName = new Label();
    public Label email = new Label();
    public Label home = new Label();
    public Label bio = new Label();
    public Label currentStatus = new Label();
    public Label permissionGroup = new Label();

    public UserInfoController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userId.setText("User Id: " + getAccountManager().getLoggedInUser().getUid());
        userName.setText("User Name: " + getAccountManager().getLoggedInUser().getName());
        email.setText("Email: " + getAccountManager().getLoggedInUser().getEmail());
        permissionGroup.setText("PermissionGroup: " + getAccountManager().getLoggedInUser().getPermissionGroup());
        // address book + if user didnt input address
        // home.setText("Location: " + user.getAddressBook().getSelectedAddress().getCity() + ", " + user.getAddress().getCountry());
        bio.setText("Bio: ");
        currentStatus.setText("Current Status: " + getAccountManager().getLoggedInUser().getUid());

        JFXButton changePassword = new JFXButton();
    }
}
