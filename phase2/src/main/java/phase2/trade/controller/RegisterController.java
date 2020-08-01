package phase2.trade.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import phase2.trade.database.Callback;
import phase2.trade.user.User;
import phase2.trade.validator.ValidatorBind;
import phase2.trade.validator.ValidatorType;
import phase2.trade.view.SceneFactory;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController extends AbstractController implements Initializable {

    private final AccountManager accountManager;

    private StringProperty submissionResultProperty;

    public Label submissionResult;

    private final SceneFactory sceneFactory = new SceneFactory();

    public TextField username, email, password;

    public RegisterController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void signUpButtonClicked(ActionEvent actionEvent) {
        ValidatorBind validatorBind = new ValidatorBind(submissionResultProperty).validate(ValidatorType.USER_NAME, "Invalid UserName", username.getText())
                .validate(ValidatorType.EMAIL, "Invalid Email", email.getText()).validate(ValidatorType.PASSWORD, "Invalid Password", password.getText());
        if (!validatorBind.isAllPass()) return;
        submissionResultProperty.setValue("Signing up..");
        accountManager.register(result -> {
            if (result != null) {
                Platform.runLater(() ->
                        switchScene("personal_dashboard.fxml",
                                new DashboardController(accountManager), actionEvent, true));
            } else {
                Platform.runLater(() -> submissionResultProperty.setValue("Username / Email already exists"));
            }
        }, username.getText(), email.getText(), password.getText());
    }

    public void goToSignIn(ActionEvent actionEvent) {
        switchScene("login.fxml",
                new LoginController(accountManager), actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submissionResultProperty = new SimpleStringProperty("");
        submissionResult.textProperty().bind(submissionResultProperty);
    }
}
