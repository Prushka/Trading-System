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

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private final AccountManager accountManager;

    private StringProperty submissionPrompt;

    public Label invalidUserNameLabel;

    public TextField usernameOrEmail, password;

    private final SceneFactory sceneFactory = new SceneFactory();

    public LoginController(UserDAO userDAO) {
        this.accountManager = new AccountManager(userDAO);
    }

    LoginController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void loginButtonClicked(ActionEvent actionEvent) {
        accountManager.login(result -> {
            if (result != null) {
                switchScene("personal_dashboard.fxml",
                        new DashboardController(), actionEvent);
            } else {
                Platform.runLater(() -> submissionPrompt.setValue("Invalid Username / Password"));
            }
        }, usernameOrEmail.getText(), password.getText());
    }

    public void signUpButtonClicked(ActionEvent actionEvent) {
        switchScene("register.fxml", new RegisterController(accountManager), actionEvent);
    }

    private void switchScene(String fileName, Object controller, ActionEvent actionEvent) {
        FXMLLoader loader = sceneFactory.getLoader(fileName);
        loader.setController(controller);
        try {
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submissionPrompt = new SimpleStringProperty("");
        invalidUserNameLabel.textProperty().bind(submissionPrompt);
    }
}
