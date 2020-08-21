package phase2.trade.user.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

/**
 * The type Login controller.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "login.fxml")
public class LoginController extends AbstractController implements Initializable {

    @FXML
    private Label submissionResult;

    @FXML
    private TextField usernameOrEmail, password;

    private StringProperty submissionResultProperty;

    /**
     * Constructs a new Login controller.
     *
     * @param controllerResources the controller resources
     */
    public LoginController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    /**
     * Login button clicked.
     *
     * @param actionEvent the action event
     */
    public void loginButtonClicked(ActionEvent actionEvent) {
        ValidatorBind validatorBind = new ValidatorBind(submissionResultProperty).validate(ValidatorType.USER_NAME, "Invalid UserName", usernameOrEmail.getText())
                .validate(ValidatorType.PASSWORD, "Invalid Password", password.getText());
        if (!validatorBind.isAllPass()) return;
        submissionResultProperty.setValue("Signing in..");
        getAccountManager().login((result, status) -> {
            status.setFailed(() -> submissionResultProperty.setValue("Invalid Username / Password"));
            status.setSucceeded(() -> getSceneManager().switchScene(DashboardController::new));
            status.handle(getNotificationFactory());
        }, usernameOrEmail.getText(), password.getText());
    }

    /**
     * Go to sign up.
     *
     * @param actionEvent the action event
     */
    public void goToSignUp(ActionEvent actionEvent) {
        getSceneManager().switchScene(RegisterController::new);
    }


    /**
     * Go to guest.
     *
     * @param actionEvent the action event
     */
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
