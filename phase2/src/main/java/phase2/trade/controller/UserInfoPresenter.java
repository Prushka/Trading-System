package phase2.trade.controller;

import com.j256.ormlite.stmt.query.In;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import phase2.trade.user.User;

import java.util.ResourceBundle;

public class UserInfoPresenter {

    public Label userId = new Label("");

    public Label userName = new Label("");

    public Label email = new Label("");

    public Label bio = new Label("");

    public Label currentStatus = new Label("");

    private User user;

    public UserInfoPresenter(User user) {
        this.user = user;
    }

    public Parent getPane() { // resource bundle
        userId.setText("User Id: "+user.getUid());
        userName.setText("User Name: "+user.getUserName());
        email.setText("Email: "+user.getEmail());
        bio.setText("Bio: ");
        currentStatus.setText("Current Status: "+user.getUid());

        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setPadding(new Insets(20, 20, 20, 20));
        vBox.getChildren().addAll(userId, userName, email, new Separator(), bio, currentStatus);
        return vBox;
    }

}
