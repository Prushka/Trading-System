package phase2.trade.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import phase2.trade.database.DatabaseResourceBundle;
import phase2.trade.database.UserDAO;
import phase2.trade.user.AccountManager;
import phase2.trade.user.User;
import phase2.trade.validator.ValidatorBind;
import phase2.trade.validator.ValidatorType;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends AbstractController implements Initializable {

    public Label submissionResult;

    public TextField usernameOrEmail, password;

    private final AccountManager accountManager;

    private StringProperty submissionResultProperty;

    public LoginController(DatabaseResourceBundle databaseResourceBundle) {
        this(databaseResourceBundle, new AccountManager(new UserDAO(databaseResourceBundle)));
    }

    LoginController(DatabaseResourceBundle databaseResourceBundle, AccountManager accountManager) {
        super(databaseResourceBundle);
        this.accountManager = accountManager;
    }

    public void loginButtonClicked(ActionEvent actionEvent) {
        ValidatorBind validatorBind = new ValidatorBind(submissionResultProperty).validate(ValidatorType.USER_NAME, "Invalid UserName", usernameOrEmail.getText())
                .validate(ValidatorType.PASSWORD, "Invalid Password", password.getText());
        if (!validatorBind.isAllPass()) return;
        submissionResultProperty.setValue("Signing in..");
        accountManager.login(result -> {
            if (result != null) {
                Platform.runLater(() ->
                        switchScene("personal_dashboard.fxml",
                                new DashboardController(databaseResourceBundle, accountManager), actionEvent, true));
            } else {
                Platform.runLater(() -> submissionResultProperty.setValue("Invalid Username / Password"));
            }
        }, usernameOrEmail.getText(), password.getText());
    }

    public void goToSignUp(ActionEvent actionEvent) {
        switchScene("register.fxml", new RegisterController(databaseResourceBundle, accountManager), actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submissionResultProperty = new SimpleStringProperty("");
        submissionResult.textProperty().bind(submissionResultProperty);
    }
}
