package phase2.trade.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import phase2.trade.callback.ResultStatus;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.presenter.SceneManager;
import phase2.trade.user.AccountManager;
import phase2.trade.validator.ValidatorBind;
import phase2.trade.validator.ValidatorType;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController extends AbstractController implements Initializable {

    private StringProperty submissionResultProperty;

    public Label submissionResult;

    public TextField username, email, password, country, city;

    public JFXButton registerButton;

    public RegisterController(SceneManager sceneManager) {
        super(sceneManager);
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
        getAccountManager().register((result, status) -> {
            if (status != ResultStatus.SUCCEEDED) {
                Platform.runLater(() -> {
                    registerButton.setDisable(false);
                    submissionResultProperty.setValue("Username / Email already exists");
                });
            } else {
                Platform.runLater(() -> {
                    getSceneManager().switchScene("dashboard.fxml", DashboardController::new);
                });
            }
        }, username.getText(), email.getText(), password.getText(), country.getText(), city.getText());
    }

    public void goToSignIn(ActionEvent actionEvent) {
        getSceneManager().switchScene("login.fxml", LoginController::new);
    }

    public void goToGuest(ActionEvent actionEvent) {
        getAccountManager().loginAsGuest();
        getSceneManager().switchScene("dashboard.fxml", DashboardController::new);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submissionResultProperty = new SimpleStringProperty("");
        submissionResult.textProperty().bind(submissionResultProperty);
    }
}
