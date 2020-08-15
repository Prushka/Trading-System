package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import phase2.trade.address.Address;
import phase2.trade.avatar.Avatar;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.EditableController;
import phase2.trade.editor.UserEditor;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.user.AccountState;
import phase2.trade.user.User;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

// TODO: ALL Controllers that don't extend AbstractTable... is a mess
//  Refactor everything in the future
@ControllerProperty(viewFile = "abstract_h.fxml")
public class UserInfoController extends EditableController<User, UserEditor> implements Initializable {

    @FXML
    private HBox root;

    private final VBox userInfoBox;

    private final VBox left = new VBox(10);
    private final VBox right = new VBox(10);

    public UserInfoController(ControllerResources controllerResources, VBox userInfoBox) {
        super(controllerResources, UserEditor::new);
        this.userInfoBox = userInfoBox;
    }

    private void generateLeft() {
        User userToPresent = getAccountManager().getLoggedInUser();
        if (userToPresent == null) return;
        Label userId, userName, email, permissionGroup, permissions, country, province, city, accountState;
        country = city = province = null;
        userId = new Label("User Id: " + userToPresent.getUid());
        userName = new Label("User Name: " + userToPresent.getName());
        email = new Label("Email: " + userToPresent.getEmail());
        permissionGroup = new Label("PermissionGroup: " + userToPresent.getPermissionGroup());

        permissions = new Label("Permissions:");
        left.getChildren().addAll(userId, userName, email, permissionGroup, permissions);

        StringBuilder permissionsS = new StringBuilder();
        userToPresent.getPermissionSet().getPerm().forEach(permission -> left.getChildren().addAll(new Label(permission.toString())));


        Address address = userToPresent.getAddressBook().getSelectedAddress();
        if (address != null) {
            country = new Label("Country: " + address.getCountry());
            province = new Label("Province: " + address.getTerritory());
            city = new Label("City: " + address.getCity());
        }
        accountState = new Label("Current Status: " + userToPresent.getAccountState());
        left.getChildren().addAll(country, province, city, accountState);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.getChildren().setAll(left, right);
        root.setSpacing(100);
        left.setSpacing(10);
        left.setStyle("-fx-font-size: 20");
        generateLeft();
        if (getAccountManager().getLoggedInUser().getPermissionGroup().equals(PermissionGroup.GUEST)) return;


        ToggleGroup group = new ToggleGroup();
        putLanguageValue(AccountState.NORMAL.name(), "state.normal");
        putLanguageValue(AccountState.ON_VOCATION.name(), "state.on.vocation");
        RadioButton normalRadio = getNodeFactory().getDefaultRadioButton(getLanguageByValue(AccountState.NORMAL.name()), group);
        RadioButton onVocationRadio = getNodeFactory().getDefaultRadioButton(getLanguageByValue(AccountState.ON_VOCATION.name()), group);

        switch (getAccountManager().getLoggedInUser().getAccountState()) {
            case NORMAL:
                normalRadio.setSelected(true);
                break;
            case ON_VOCATION:
                onVocationRadio.setSelected(true);
                break;
        }
        EventHandler<ActionEvent> accountStateHandler = event -> {
            shortenAlter(getAccountManager().getLoggedInUser(),
                    getValueByLanguage(((RadioButton) group.getSelectedToggle()).getText()),
                    s -> {
                    }, UserEditor::alterAccountState);
        };

        normalRadio.setOnAction(accountStateHandler);
        onVocationRadio.setOnAction(accountStateHandler);


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

        GeneralVBoxAlert addressAlert = getPopupFactory().vBoxAlert("Modify Your Address", "");
        ComboBox<String> country = getNodeFactory().getComboBox(getConfigBundle().getGeoConfig().getMap().keySet());
        ComboBox<String> province = getNodeFactory().getComboBox();
        ComboBox<String> city = getNodeFactory().getComboBox();
        TextField addressLine1 = getNodeFactory().getDefaultTextField("Address Line 1 (Optional)");
        TextField addressLine2 = getNodeFactory().getDefaultTextField("Address Line 2 (Optional)");
        TextField postalCode = getNodeFactory().getDefaultTextField("Postal Code (Optional)");
        country.setOnAction(e -> {
            String selectedCountry = country.getSelectionModel().getSelectedItem();
            if (selectedCountry != null) {
                ObservableList<String> provinces = FXCollections.observableArrayList(getConfigBundle().getGeoConfig().getMap().get(selectedCountry).keySet());
                Collections.sort(provinces);
                province.setItems(provinces);
            }
        });

        province.setOnAction(e -> {
            String selectedCountry = country.getSelectionModel().getSelectedItem();
            String selectedProvince = province.getSelectionModel().getSelectedItem();
            if (selectedCountry != null) {
                ObservableList<String> cities = FXCollections.observableArrayList(getConfigBundle().getGeoConfig().getMap().get(selectedCountry).get(selectedProvince));
                Collections.sort(cities);
                city.setItems(cities);
            }
        });

        addressAlert.addNodes(country, province, city, addressLine1, addressLine2, postalCode);
        addressAlert.setEventHandler(event -> {
            UserEditor userEditor = new UserEditor(getAccountManager().getLoggedInUser(), getConfigBundle());
            userEditor.addAddress(country.getSelectionModel().getSelectedItem(), province.getSelectionModel().getSelectedItem(), city.getSelectionModel().getSelectedItem()
                    , addressLine1.getText(), addressLine2.getText(), postalCode.getText());
            updateEntity(getAccountManager().getLoggedInUser());
        });

        Button changePassword = getNodeFactory().getDefaultRippleButton("Change Password");
        Button changeUserName = getNodeFactory().getDefaultRippleButton("Change User Name");
        Button modifyAddress = getNodeFactory().getDefaultRippleButton("Modify Address");
        changeUserName.setOnAction(event -> userNameAlert.display());
        changePassword.setOnAction(event -> passwordAlert.display());
        modifyAddress.setOnAction(event -> addressAlert.display());

        final Button changeAvatar = new JFXButton("Choose Avatar");

        changeAvatar.setOnAction((final ActionEvent e) -> {
            uploadAvatar();
        });

        right.getChildren().addAll(changePassword, changeUserName, normalRadio, onVocationRadio, changeAvatar, modifyAddress);
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
