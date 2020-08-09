package phase2.trade.controller;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import phase2.trade.user.User;

import java.net.URL;
import java.util.ResourceBundle;

public class UserInfoPresenter implements Initializable {

    public Label userId = new Label("");

    public Label userName = new Label("");

    public Label email = new Label("");

    public Label home = new Label("");

    public Label bio = new Label("");

    public Label currentStatus = new Label("");

    private final User user;

    public UserInfoPresenter(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userId.setText("User Id: " + user.getUid());
        userName.setText("User Name: " + user.getUserName());
        email.setText("Email: " + user.getEmail());
        // address book + if user didnt input address
        // home.setText("Location: " + user.getAddressBook().getSelectedAddress().getCity() + ", " + user.getAddress().getCountry());
        bio.setText("Bio: ");
        currentStatus.setText("Current Status: " + user.getUid());
    }
}
