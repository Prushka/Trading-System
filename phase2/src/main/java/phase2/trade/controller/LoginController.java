package phase2.trade.controller;

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

public class LoginController {

    private final AccountManager accountManager;

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
        invalidUserNameLabel.setVisible(true);
        accountManager.login(usernameOrEmail.getText(), password.getText());
    }

    public void signUpButtonClicked(ActionEvent actionEvent) throws IOException {
        RegisterController registerController = new RegisterController(accountManager);
        FXMLLoader loader = sceneFactory.getLoader("register.fxml");
        loader.setController(registerController);
        ((Node) actionEvent.getSource()).getScene().setRoot(loader.load());
    }
}
