package phase2.trade.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import phase2.trade.view.SceneFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController {

    private final AccountManager accountManager;

    private final SceneFactory sceneFactory = new SceneFactory();

    public RegisterController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void signUpButtonClicked(ActionEvent actionEvent) {

    }

    public void goToSignIn(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().setRoot(sceneFactory.getPane("login.xml", new LoginController(accountManager)));
    }
}
