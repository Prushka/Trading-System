package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import phase2.trade.avatar.Avatar;
import phase2.trade.avatar.PersistAvatarCommand;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Willingness;
import phase2.trade.user.AccountState;
import phase2.trade.user.command.ChangePassword;
import phase2.trade.user.command.ChangeUserName;
import phase2.trade.user.command.UpdateUsers;
import phase2.trade.view.window.GeneralVBoxAlert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// TODO: ALL Controllers that don't extend AbstractTable... is a mess
//  Refactor everything in the future
public class UserInfoController extends AbstractController implements Initializable {

    @FXML
    private VBox root;

    private final VBox userInfoBox;

    public UserInfoController(ControllerResources controllerResources, VBox userInfoBox) {
        super(controllerResources);
        this.userInfoBox = userInfoBox;
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

        Button changePassword = new JFXButton("Change Password");
        Button changeUserName = new JFXButton("Change User Name");

        ToggleGroup group = new ToggleGroup();
        putLanguageValue(AccountState.NORMAL.name(), "state.normal");
        putLanguageValue(AccountState.ON_VOCATION.name(), "state.on.vocation");
        RadioButton normalRadio = getNodeFactory().getDefaultRadioButton(getLanguageByValue(AccountState.NORMAL.name()), group);
        RadioButton onVocationRadio = getNodeFactory().getDefaultRadioButton(getLanguageByValue(AccountState.ON_VOCATION.name()), group);

        switch (getAccountManager().getLoggedInUser().getAccountState()){
            case NORMAL:
                normalRadio.setSelected(true);
                break;
            case ON_VOCATION:
                onVocationRadio.setSelected(true);
                break;
        }
        EventHandler<ActionEvent> accountStateHandler = event->{

        };

        TextField oldPassword = getNodeFactory().getDefaultTextField("Old Password");
        TextField newPassword = getNodeFactory().getDefaultTextField("New Password");

        TextField password = getNodeFactory().getDefaultTextField("Password");
        TextField newUserName = getNodeFactory().getDefaultTextField("New User Name");


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

        final Button changeAvatar = new JFXButton("Choose Avatar");

        changeAvatar.setOnAction((final ActionEvent e) -> {
            uploadAvatar();
        });

        root.getChildren().addAll(changePassword, changeUserName, normalRadio, onVocationRadio, changeAvatar);
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
                PersistAvatarCommand persistAvatarCommand = getCommandFactory().getCommand(PersistAvatarCommand::new, c -> c.setAvatar(avatar));
                persistAvatarCommand.execute((result, status) -> {
                    status.setSucceeded(() -> {
                        UpdateUsers update = getCommandFactory().getCommand(UpdateUsers::new, c -> c.setUserToUpdate(
                                getAccountManager().getLoggedInUser()));
                        update.execute((result1, status1) -> {
                            status1.setSucceeded(() -> userInfoBox.getChildren().setAll
                                    (getSceneManager().loadPane(UserSideInfoController::new)));
                            status1.handle(getPopupFactory());
                        });
                    });
                    status.handle(getPopupFactory());
                });

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
