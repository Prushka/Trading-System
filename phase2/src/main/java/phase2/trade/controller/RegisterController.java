package phase2.trade.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import phase2.trade.database.DatabaseResourceBundle;
import phase2.trade.user.AccountManager;
import phase2.trade.validator.ValidatorBind;
import phase2.trade.validator.ValidatorType;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController extends AbstractController implements Initializable {

    private final AccountManager accountManager;

    private StringProperty submissionResultProperty;

    public Label submissionResult;

    public TextField username, email, password, country, city;

    public RegisterController(DatabaseResourceBundle databaseResourceBundle, AccountManager accountManager) {
        super(databaseResourceBundle);
        this.accountManager = accountManager;
    }

    public void signUpButtonClicked(ActionEvent actionEvent) {
        ValidatorBind validatorBind = new ValidatorBind(submissionResultProperty).validate(ValidatorType.USER_NAME, "Invalid UserName", username.getText())
                .validate(ValidatorType.EMAIL, "Invalid Email", email.getText()).validate(ValidatorType.PASSWORD, "Invalid Password", password.getText());
        if (!validatorBind.isAllPass()) return;
        submissionResultProperty.setValue("Signing up..");
        accountManager.register(result -> {
            if (result != null) {
                Platform.runLater(() ->
                        switchScene("personal_dashboard.fxml",
                                new DashboardController(databaseResourceBundle, accountManager), actionEvent, true));
            } else {
                Platform.runLater(() -> submissionResultProperty.setValue("Username / Email already exists"));
            }
        }, username.getText(), email.getText(), password.getText(), country.getText(), city.getText());
    }

    public void goToSignIn(ActionEvent actionEvent) {
        switchScene("login.fxml",
                new LoginController(databaseResourceBundle, accountManager), actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submissionResultProperty = new SimpleStringProperty("");
        submissionResult.textProperty().bind(submissionResultProperty);
    }
}
