package phase2.trade.controller.user;

import com.jfoenix.controls.JFXButton;
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

@ControllerProperty(viewFile = "register.fxml")
public class RegisterController extends AbstractController implements Initializable {

    private StringProperty submissionResultProperty;

    public Label submissionResult;

    public TextField username, email, password, country, city;

    public JFXButton registerButton;

    public RegisterController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    public void registerButtonClicked(ActionEvent actionEvent) {
        registerButton.setDisable(true);
        ValidatorBind validatorBind = new ValidatorBind(submissionResultProperty).validate(ValidatorType.USER_NAME, "Invalid UserName", username.getText())
                .validate(ValidatorType.EMAIL, "Invalid Email", email.getText()).validate(ValidatorType.PASSWORD, "Invalid Password", password.getText());
        if (!validatorBind.isAllPass()) {
            registerButton.setDisable(false);
            return;
        }
        submissionResultProperty.setValue("Signing up..");
        getAccountManager().register((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                getSceneManager().switchScene(DashboardController::new);
            });
            resultStatus.setFailed(() -> {
                registerButton.setDisable(false);
                submissionResultProperty.setValue("Username / Email already exists");
            });
            resultStatus.handle(getPopupFactory());
        }, username.getText(), email.getText(), password.getText(), country.getText(), city.getText());
    }

    public void goToSignIn(ActionEvent actionEvent) {
        getSceneManager().switchScene(LoginController::new);
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
