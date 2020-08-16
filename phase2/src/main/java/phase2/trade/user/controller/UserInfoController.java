package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import phase2.trade.avatar.Avatar;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.EditableController;
import phase2.trade.editor.UserEditor;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.user.User;
import phase2.trade.user.command.ChangePassword;
import phase2.trade.user.command.ChangeUserName;
import phase2.trade.user.command.UpdateUsers;
import phase2.trade.view.widget.*;
import phase2.trade.view.window.GeneralVBoxAlert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

// TODO: ALL Controllers that don't extend AbstractTable... is a mess
//  Refactor everything in the future
@ControllerProperty(viewFile = "abstract_border.fxml")
public class UserInfoController extends EditableController<User, UserEditor> implements Initializable {

    @FXML
    private BorderPane root;

    private final VBox userInfoBox;

    private final User userToPresent;

    private Parent userWidget, permissionWidget, addressWidget, accountStateWidget;

    public UserInfoController(ControllerResources controllerResources, VBox userInfoBox) {
        super(controllerResources, UserEditor::new);
        this.userInfoBox = userInfoBox;
        userToPresent = getAccountManager().getLoggedInUser();
    }

    private void generateLeftUserPane() {
        if (userToPresent == null) return; // this shouldn't happen
        userWidget = getSceneManager().loadPane(UserWidget::new);
        addressWidget = getSceneManager().loadPane(AddressWidget::new);
        accountStateWidget = getSceneManager().loadPane(AccountStateWidget::new);
        GridPane.setConstraints(userWidget, 1, 1);
        //GridPane.setConstraints(permissionWidget, 2, 1);
        GridPane.setConstraints(addressWidget, 3, 1);
        GridPane.setConstraints(accountStateWidget, 4, 1);
        HBox top = new HBox(userWidget, addressWidget, accountStateWidget);
        top.setSpacing(20);
        root.setTop(top);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateLeftUserPane();
        if (getAccountManager().getLoggedInUser().getPermissionGroup().equals(PermissionGroup.GUEST)) return;


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


        Button changePassword = getNodeFactory().getDefaultRippleButton("Change Password");
        Button changeUserName = getNodeFactory().getDefaultRippleButton("Change User Name");
        Button modifyAddress = getNodeFactory().getDefaultRippleButton("Modify Address");
        changeUserName.setOnAction(event -> userNameAlert.display());
        changePassword.setOnAction(event -> passwordAlert.display());

        final Button changeAvatar = new JFXButton("Choose Avatar");

        changeAvatar.setOnAction((final ActionEvent e) -> {
            uploadAvatar();
        });

        VBox right = new VBox(10);
        right.getChildren().addAll(changePassword, changeUserName, changeAvatar, modifyAddress);
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

    @Override
    protected void updateEntity(List<User> entities) {
        disableButtons(true);
        Command<?> command = getCommandFactory().getCommand(UpdateUsers::new, c -> {
            c.setUsersToUpdate(entities);
        });
        command.execute((result, resultStatus) -> {
            resultStatus.setAfter(() -> {
                disableButtons(false);
            });
            resultStatus.handle(getPopupFactory());
        });
    }
}
