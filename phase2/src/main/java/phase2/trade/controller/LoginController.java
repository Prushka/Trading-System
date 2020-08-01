package phase2.trade.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public void loginButtonClicked(ActionEvent actionEvent) {

    }

    public void signUpButtonClicked(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(resources.getString("button.login.text"));
    }
}
