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

    private StringProperty submissionPrompt;

    public TextField username, email, password;

    public RegisterController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void signUpButtonClicked(ActionEvent actionEvent) {
        accountManager.register(result -> {
            if (result != null) {
                Platform.runLater(() ->
                        switchScene("personal_dashboard.fxml",
                                new DashboardController(), actionEvent, true));
            } else {
                Platform.runLater(() -> submissionResultProperty.setValue("Username / Email already exists"));
            }
        }, username.getText(), email.getText(), password.getText());
    }

    void switchScene(String fileName, Object controller, ActionEvent actionEvent) {
        System.out.println("Switching scene");
        FXMLLoader loader = sceneFactory.getLoader(fileName);
        loader.setController(controller);
        try {
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
