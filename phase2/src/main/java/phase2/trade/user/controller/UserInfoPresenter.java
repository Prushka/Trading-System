package phase2.trade.user.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "user_info_side.fxml")
public class UserInfoPresenter extends AbstractController implements Initializable {

    // the reason why they are initialized here is because many user info fxml share one presenter and some labels may not exist there

    public Label userId = new Label();
    public Label userName = new Label();
    public Label email = new Label();
    public Label home = new Label();
    public Label bio = new Label();
    public Label currentStatus = new Label();
    public Label permissionGroup = new Label();
    public ImageView imageView;

    private final User user;

    public UserInfoPresenter(ControllerResources controllerResources) {
        super(controllerResources);
        this.user = getAccountManager().getLoggedInUser();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userId.setText("User Id: " + user.getUid());
        userName.setText("User Name: " + user.getName());
        email.setText("Email: " + user.getEmail());
        permissionGroup.setText("PermissionGroup: " + user.getPermissionGroup());
        if (user.getAvatar() != null && user.getAvatar().getImageData() != null) {
            Image img = new Image(new ByteArrayInputStream(user.getAvatar().getImageData()));
            imageView.setImage(img);
        }
        // address book + if user didnt input address
        // home.setText("Location: " + user.getAddressBook().getSelectedAddress().getCity() + ", " + user.getAddress().getCountry());
        bio.setText("Bio: ");
        currentStatus.setText("Current Status: " + user.getUid());
    }
}
