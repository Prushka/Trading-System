package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import phase2.trade.address.AddressBook;
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
import phase2.trade.view.widget.SmallTextWidget;
import phase2.trade.view.widget.Widget;
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

    private Widget userWidget, permissionWidget, addressWidget, accountStateWidget;

    public UserInfoController(ControllerResources controllerResources, VBox userInfoBox) {
        super(controllerResources, UserEditor::new);
        this.userInfoBox = userInfoBox;
        userToPresent = getAccountManager().getLoggedInUser();
    }

    private void generateLeftUserPane() {
        if (userToPresent == null) return; // this shouldn't happen

        userWidget = new SmallTextWidget("gradient-j", "User - " + userToPresent.getUid(), "Name: " + userToPresent.getName(), "Email: " + userToPresent.getEmail());

        permissionWidget = new SmallTextWidget("gradient-k", "Permission", "Group: " + userToPresent.getPermissionGroup().toString(),
                "Permissions: " + userToPresent.getPermissionSet().getPerm().size());

        generateAddressWidget();
        generateAccountStateWidget();

        GridPane.setConstraints(userWidget.getRoot(), 1, 1);
        GridPane.setConstraints(permissionWidget.getRoot(), 2, 1);
        GridPane.setConstraints(addressWidget.getRoot(), 3, 1);
        GridPane.setConstraints(accountStateWidget.getRoot(), 4, 1);
        HBox top = new HBox(permissionWidget.getRoot(), userWidget.getRoot(), addressWidget.getRoot(), accountStateWidget.getRoot());
        top.setSpacing(20);
        root.setTop(top);
    }

    private void refreshAddressWidget() {

        = new Label("Address - " + country);
        Label  = new Label("Province: " + province);
        Label cityLabel = new Label("City: " + city);
    }

    private void generateAddressWidget() {
        AddressBook addressBook = userToPresent.getAddressBook();
        String country = "";
        String province = "";
        String city = "";
        GeneralVBoxAlert addressAlert = getPopupFactory().vBoxAlert("Modify Your Address", "");
        ComboBox<String> countryCombo = getNodeFactory().getComboBox(getConfigBundle().getGeoConfig().getMap().keySet());
        ComboBox<String> provinceCombo = getNodeFactory().getComboBox();
        ComboBox<String> cityCombo = getNodeFactory().getComboBox();
        TextField addressLine1 = getNodeFactory().getDefaultTextField("Address Line 1 (Optional)");
        TextField addressLine2 = getNodeFactory().getDefaultTextField("Address Line 2 (Optional)");
        TextField postalCode = getNodeFactory().getDefaultTextField("Postal Code (Optional)");

        countryCombo.setOnAction(e -> {
            String selectedCountry = countryCombo.getSelectionModel().getSelectedItem();
            if (selectedCountry != null) {
                provinceCombo.setItems(getConfigBundle().getGeoConfig().getProvincesByCountry(selectedCountry));
            }
        });

        provinceCombo.setOnAction(e -> {
            String selectedCountry = countryCombo.getSelectionModel().getSelectedItem();
            String selectedProvince = provinceCombo.getSelectionModel().getSelectedItem();
            if (selectedCountry != null) {
                cityCombo.setItems(getConfigBundle().getGeoConfig().getCitiesByProvinceCountry(selectedCountry, selectedProvince));
            }
        });

        Label countryLabel, provinceLabel, cityLabel;

        addressAlert.addNodes(countryCombo, provinceCombo, cityCombo, addressLine1, addressLine2, postalCode);
        addressAlert.setEventHandler(event -> {
            UserEditor userEditor = new UserEditor(getAccountManager().getLoggedInUser(), getConfigBundle());
            userEditor.addAddress(countryCombo.getSelectionModel().getSelectedItem(), provinceCombo.getSelectionModel().getSelectedItem(), cityCombo.getSelectionModel().getSelectedItem()
                    , addressLine1.getText(), addressLine2.getText(), postalCode.getText());
            updateEntity(getAccountManager().getLoggedInUser());
            refreshAddressWidget();
        });

        if (userToPresent.getAddressBook().getSelectedAddress() != null) {
            country = addressBook.getSelectedAddress().getCountry();
            province = addressBook.getSelectedAddress().getTerritory();
            city = addressBook.getSelectedAddress().getCity();
            countryCombo.getSelectionModel().select(country);
            provinceCombo.setItems(getConfigBundle().getGeoConfig().getProvincesByCountry(country));
            provinceCombo.getSelectionModel().select(province);
            cityCombo.setItems(getConfigBundle().getGeoConfig().getCitiesByProvinceCountry(country, province));
            cityCombo.getSelectionModel().select(city);
            addressLine1.setText(addressBook.getSelectedAddress().getFirstAddressLine());
            addressLine2.setText(addressBook.getSelectedAddress().getSecondAddressLine());
        }

        addressWidget = new SmallTextWidget("gradient-i");
        addressWidget.addTitle(countryLabel);
        addressWidget.addTitle(provinceLabel, cityLabel);
        addressWidget.setOnMouseClicked(e -> addressAlert.display());
    }

    private void generateAccountStateWidget() {
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
                        s.setSucceeded(() -> {
                            getPopupFactory().toast(2, "Updated");
                            s.handle(getPopupFactory());
                        });
                    }, UserEditor::alterAccountState);
        };
        normalRadio.getStyleClass().addAll("butter-body");
        onVocationRadio.getStyleClass().addAll("butter-body");

        normalRadio.setOnAction(accountStateHandler);
        onVocationRadio.setOnAction(accountStateHandler);
        Label title = new Label("Account State");
        title.getStyleClass().addAll("butter-header");
        accountStateWidget = new Widget("gradient-l", 270, 120);
        accountStateWidget.addNodes(title, normalRadio, onVocationRadio);
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
