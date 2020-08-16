package phase2.trade.view.widget;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import phase2.trade.avatar.Avatar;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.command.ChangePassword;
import phase2.trade.user.command.ChangeUserName;
import phase2.trade.user.command.UpdateUsers;
import phase2.trade.user.controller.UserSideInfoController;
import phase2.trade.view.window.AlertWindow;
import phase2.trade.view.window.GeneralVBoxAlert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserOptionWidget extends WidgetControllerBase {

    private final Button changePassword = getNodeFactory().getDefaultRippleButton("Change Password");
    private final Button changeUserName = getNodeFactory().getDefaultRippleButton("Change User Name");
    private final Button changeAvatar = getNodeFactory().getDefaultRippleButton("Change User Name");

    public UserOptionWidget(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    public void refresh() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-j");
        GeneralVBoxAlert passwordAlert = getPopupFactory().vBoxAlert("Change Password", "");
        TextField oldPassword = getNodeFactory().getDefaultTextField("Old Password");
        TextField newPassword = getNodeFactory().getDefaultTextField("New Password");
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
        TextField password = getNodeFactory().getDefaultTextField("Password");
        TextField newUserName = getNodeFactory().getDefaultTextField("New User Name");
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
        changeAvatar.setOnAction((final ActionEvent e) -> {
            uploadAvatar();
        });

        addNodes(changePassword,changeUserName,changeAvatar);
        refresh();
    }

    // TODO: decouple this one
    // also separate presenters
    // https://stackoverflow.com/questions/24038524/how-to-get-byte-from-javafx-imageview
    public void uploadAvatar() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(getSceneManager().getWindow());
        if (file != null) {
            // openFile(file);
            Image image = new Image(file.toURI().toString());
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream s = new ByteArrayOutputStream();
            try {
                ImageIO.write(bImage, "png", s);
                byte[] res = s.toByteArray();
                s.close();

                Avatar avatar = new Avatar();
                avatar.setImageData(res);
                getAccountManager().getLoggedInUser().setAvatar(avatar);
                UpdateUsers update = getCommandFactory().getCommand(UpdateUsers::new, c -> c.setUserToUpdate(
                        getAccountManager().getLoggedInUser()));
                update.execute((result1, status1) -> {
                    status1.setSucceeded(() -> userInfoBox.getChildren().setAll
                            (getSceneManager().loadPane(UserSideInfoController::new)));
                    status1.handle(getPopupFactory());
                });

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
