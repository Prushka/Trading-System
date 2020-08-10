package phase2.trade.controller;

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

public class LoginController extends AbstractController implements Initializable {

    public Label submissionResult;

    public TextField usernameOrEmail, password;

    private StringProperty submissionResultProperty;

    public LoginController(SceneManager sceneManager) {
        super(sceneManager);
    }

    public void loginButtonClicked(ActionEvent actionEvent) {
        ValidatorBind validatorBind = new ValidatorBind(submissionResultProperty).validate(ValidatorType.USER_NAME, "Invalid UserName", usernameOrEmail.getText())
                .validate(ValidatorType.PASSWORD, "Invalid Password", password.getText());
        if (!validatorBind.isAllPass()) return;
        submissionResultProperty.setValue("Signing in..");
        getAccountManager().login((result, status) -> {
            if (status != ResultStatus.SUCCEEDED) {
                Platform.runLater(() ->
                {
                    submissionResultProperty.setValue("Invalid Username / Password");
                });
            } else {
                Platform.runLater(() ->
                        getSceneManager().switchScene("dashboard.fxml", DashboardController::new));
            }
        }, usernameOrEmail.getText(), password.getText());
    }

    public void goToSignUp(ActionEvent actionEvent) {
        getSceneManager().switchScene("register.fxml", RegisterController::new);
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
