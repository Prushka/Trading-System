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
import phase2.trade.database.UserDAO;
import phase2.trade.view.SceneFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends AbstractController implements Initializable {

    private final AccountManager accountManager;

    private StringProperty submissionResultProperty;

    public Label submissionResult;

    public TextField usernameOrEmail, password;

    public LoginController(UserDAO userDAO) {
        this.accountManager = new AccountManager(userDAO);
    }

    LoginController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void loginButtonClicked(ActionEvent actionEvent) {
        accountManager.login(result -> {
            if (result != null) {
                Platform.runLater(() ->
                        switchScene("personal_dashboard.fxml",
                                new DashboardController(), actionEvent, true));
            } else {
                Platform.runLater(() -> submissionResultProperty.setValue("Invalid Username / Password"));
            }
        }, usernameOrEmail.getText(), password.getText());
    }

    public void signUpButtonClicked(ActionEvent actionEvent) {
        switchScene("register.fxml", new RegisterController(accountManager), actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submissionResultProperty = new SimpleStringProperty("");
        submissionResult.textProperty().bind(submissionResultProperty);
    }
}
