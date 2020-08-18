package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.DashboardController;
import phase2.trade.validator.ValidatorBind;
import phase2.trade.validator.ValidatorType;

import java.net.URL;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "register.fxml")
public class RegisterController extends AbstractController implements Initializable {

    @FXML
    private Label submissionResult;

    @FXML
    private TextField username, email, password;

    @FXML
    private JFXButton registerButton;

    @FXML
    private VBox inputArea;

    private StringProperty submissionResultProperty;

    private ComboBox<String> countryCombo;
    private ComboBox<String> provinceCombo;
    private ComboBox<String> cityCombo;

    public RegisterController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    public void registerButtonClicked(ActionEvent actionEvent) {
        registerButton.setDisable(true);
        ValidatorBind validatorBind = new ValidatorBind(submissionResultProperty).validate(ValidatorType.USER_NAME, "Invalid UserName", username.getText())
                .validate(ValidatorType.EMAIL, "Invalid Email", email.getText()).validate(ValidatorType.PASSWORD, "Invalid Password", password.getText());
        if (!validatorBind.isAllPass()) {
            registerButton.setDisable(false);
            return;
        }
        submissionResultProperty.setValue("Signing up..");
        getAccountManager().register((result, resultStatus) -> {
                    resultStatus.setSucceeded(() -> {
                        publishGateway(UserManageController.class);
                        getSceneManager().switchScene(DashboardController::new);
                    });
                    resultStatus.setFailed(() -> {
                        submissionResultProperty.setValue(resultStatus.getMessage());
                    });
                    resultStatus.setAfter(() -> {
                        registerButton.setDisable(false);
                    });
                    resultStatus.handle(getNotificationFactory());
                }, username.getText(), email.getText(), password.getText(),
                countryCombo.getSelectionModel().getSelectedItem(),
                provinceCombo.getSelectionModel().getSelectedItem(), cityCombo.getSelectionModel().getSelectedItem());
    }

    public void goToSignIn(ActionEvent actionEvent) {
        getSceneManager().switchScene(LoginController::new);
    }

    public void goToGuest(ActionEvent actionEvent) {
        getAccountManager().loginAsGuest();
        getSceneManager().switchScene(DashboardController::new);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submissionResultProperty = new SimpleStringProperty("");
        submissionResult.textProperty().bind(submissionResultProperty);

        getNodeFactory().getAddressComboBoxes((a, b, c) -> {
            countryCombo = a;
            provinceCombo = b;
            cityCombo = c;
        }, getConfigBundle().getGeoConfig());
        countryCombo.setPrefWidth(300);
        provinceCombo.setPrefWidth(300);
        cityCombo.setPrefWidth(300);
        inputArea.getChildren().addAll(countryCombo, provinceCombo, cityCombo);
    }
}
