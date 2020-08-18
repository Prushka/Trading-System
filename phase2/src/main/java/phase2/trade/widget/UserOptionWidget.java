package phase2.trade.widget;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import phase2.trade.alert.VBoxAlert;
import phase2.trade.avatar.UploadAvatarController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.refresh.ReType;
import phase2.trade.user.command.ChangePassword;
import phase2.trade.user.command.ChangeUserName;
import phase2.trade.user.controller.UserSideInfoController;

import java.net.URL;
import java.util.ResourceBundle;

public class UserOptionWidget extends WidgetControllerBase {

    private final Button changePassword = getNodeFactory().getDefaultFlatButton("Change Password", "widget-button");
    private final Button changeUserName = getNodeFactory().getDefaultFlatButton("Change User Name", "widget-button");
    private final Button changeAvatar = getNodeFactory().getDefaultFlatButton("Change Avatar", "widget-button");

    public UserOptionWidget(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    public void refresh() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-j");
        VBoxAlert passwordAlert = getNotificationFactory().vBoxAlert("Change Password", "");
        TextField oldPassword = getNodeFactory().getDefaultTextField("Old Password");
        TextField newPassword = getNodeFactory().getDefaultTextField("New Password");
        passwordAlert.addNodes(oldPassword, newPassword);
        passwordAlert.setConfirmHandler(event -> {
            ChangePassword changePasswordCommand = getCommandFactory().getCommand(ChangePassword::new);
            changePasswordCommand.execute(((result, status) -> {
                        status.setSucceeded(() -> {
                            publish(ReType.REFRESH, UserSideInfoController.class, UserWidget.class);
                            getNotificationFactory().toast(2, "Updated!");
                        });
                        status.handle(getNotificationFactory());
                    }
                    ),
                    getAccountManager().getLoggedInUser().getName(), oldPassword.getText(), newPassword.getText());
        });

        VBoxAlert userNameAlert = getNotificationFactory().vBoxAlert("Change UserName", "");
        TextField password = getNodeFactory().getDefaultTextField("Password");
        TextField newUserName = getNodeFactory().getDefaultTextField("New User Name");
        userNameAlert.addNodes(password, newUserName);
        userNameAlert.setConfirmHandler(event -> {
            ChangeUserName command = getCommandFactory().getCommand(ChangeUserName::new);
            command.execute(((result, status) -> {
                        status.setFailed(() -> getNotificationFactory().toast(5, "Cannot verify the information you provided. Check your password."));
                        status.setExist(() -> getNotificationFactory().toast(5, "Such User Name Already Exists"));
                        status.setSucceeded(() -> {
                            publish(ReType.REFRESH, UserSideInfoController.class, UserWidget.class);
                            getNotificationFactory().toast(2, "Updated!");
                        });
                        status.handle(getNotificationFactory());
                    }
                    ),
                    getAccountManager().getLoggedInUser().getName(), password.getText(), newUserName.getText());
        });

        UploadAvatarController uploadAvatarController = getControllerFactory().getController(UploadAvatarController::new);

        changeUserName.setOnAction(event -> userNameAlert.display());
        changePassword.setOnAction(event -> passwordAlert.display());
        changeAvatar.setOnAction((final ActionEvent e) -> {
            uploadAvatarController.uploadAvatar();
        });

        addContent(changePassword, changeUserName, changeAvatar);
        refresh();
    }

}
