package phase2.trade.controller.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.DashboardController;
import phase2.trade.validator.ValidatorBind;
import phase2.trade.validator.ValidatorType;

import java.net.URL;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "login.fxml")
public class LoginController extends AbstractController implements Initializable {

    public Label submissionResult;

    public TextField usernameOrEmail, password;

    private StringProperty submissionResultProperty;

    public LoginController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    public void loginButtonClicked(ActionEvent actionEvent) {
        ValidatorBind validatorBind = new ValidatorBind(submissionResultProperty).validate(ValidatorType.USER_NAME, "Invalid UserName", usernameOrEmail.getText())
                .validate(ValidatorType.PASSWORD, "Invalid Password", password.getText());
        if (!validatorBind.isAllPass()) return;
        submissionResultProperty.setValue("Signing in..");
        getAccountManager().login((result, status) -> {
            status.setFailed(() -> submissionResultProperty.setValue("Invalid Username / Password"));
            status.setSucceeded(() -> getSceneManager().switchScene(DashboardController::new));
            status.handle(getPopupFactory());
        }, usernameOrEmail.getText(), password.getText());
    }

    public void goToSignUp(ActionEvent actionEvent) {
        getSceneManager().switchScene(RegisterController::new);
    }


    public void goToGuest(ActionEvent actionEvent) {
        getAccountManager().loginAsGuest();
        getSceneManager().switchScene(DashboardController::new);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submissionResultProperty = new SimpleStringProperty("");
        submissionResult.textProperty().bind(submissionResultProperty);
    }
}
