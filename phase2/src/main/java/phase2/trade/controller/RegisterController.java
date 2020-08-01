package phase2.trade.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import phase2.trade.view.SceneFactory;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController {

    private final AccountManager accountManager;

    private final SceneFactory sceneFactory = new SceneFactory();

    public TextField username, email, password;

    public RegisterController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void signUpButtonClicked(ActionEvent actionEvent) {
        accountManager.register(username.getText(), email.getText(), password.getText());
    }

    public void goToSignIn(ActionEvent actionEvent) throws IOException {
        LoginController registerController = new LoginController(accountManager);
        FXMLLoader loader = sceneFactory.getLoader("login.fxml");
        loader.setController(registerController);
        ((Node) actionEvent.getSource()).getScene().setRoot(loader.load());
    }
}
