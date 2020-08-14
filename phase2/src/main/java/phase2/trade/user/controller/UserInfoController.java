package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.command.ChangePassword;
import phase2.trade.user.command.ChangeUserName;
import phase2.trade.view.window.GeneralVBoxAlert;

import java.net.URL;
import java.util.ResourceBundle;

public class UserInfoController extends AbstractController implements Initializable {

    @FXML
    private VBox root;

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
        JFXButton changeUserName = new JFXButton("Change User Name");

        TextField oldPassword = getNodeFactory().getDefaultTextField("Old Password");
        TextField newPassword = getNodeFactory().getDefaultTextField("New Password");

        TextField password = getNodeFactory().getDefaultTextField("Password");
        TextField newUserName = getNodeFactory().getDefaultTextField("New User Name");

        root.getChildren().addAll(changePassword, changeUserName);

        GeneralVBoxAlert passwordAlert = getPopupFactory().vBoxAlert("Change Password", "");
        passwordAlert.addNodes(oldPassword, newPassword);
        passwordAlert.setEventHandler(event -> {
            ChangePassword changePasswordCommand = getCommandFactory().getCommand(ChangePassword::new);
            changePasswordCommand.execute(((result, status) -> {
                        status.setFailed(() -> getPopupFactory().toast(5, "Cannot verify the information you provided. Check your password."));
                        status.handle(getPopupFactory());
                    }
                    ),
                    getAccountManager().getLoggedInUser().getName(), oldPassword.getText(), newPassword.getText());
        });

        GeneralVBoxAlert userNameAlert = getPopupFactory().vBoxAlert("Change UserName", "");
        userNameAlert.addNodes(password, newUserName);
        userNameAlert.setEventHandler(event -> {
            ChangeUserName command = getCommandFactory().getCommand(ChangeUserName::new);
            command.execute(((result, status) -> {
                        status.setFailed(() -> getPopupFactory().toast(5, "Cannot verify the information you provided. Check your password."));
                        status.setExist(() -> getPopupFactory().toast(5, "Such User Name Already Exists"));
                        status.handle(getPopupFactory());
                    }
                    ),
                    getAccountManager().getLoggedInUser().getName(), password.getText(), newUserName.getText());
        });

        changeUserName.setOnAction(event -> userNameAlert.display());
        changePassword.setOnAction(event -> passwordAlert.display());

    }
}
