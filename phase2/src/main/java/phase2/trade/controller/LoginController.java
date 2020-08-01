package phase2.trade.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import phase2.trade.view.SceneFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {

    private final AccountManager accountManager;

    public Label invalidUserNameLabel;

    private final SceneFactory sceneFactory = new SceneFactory();

    public LoginController(UserRepository userRepository) {
        this.accountManager = new AccountManager(userRepository);
    }

    LoginController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void loginButtonClicked(ActionEvent actionEvent) {
        invalidUserNameLabel.setVisible(true);
    }

    public void signUpButtonClicked(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().setRoot(sceneFactory.getPane("register.xml", new RegisterController(accountManager)));
    }

}
