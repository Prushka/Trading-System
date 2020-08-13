package phase2.trade.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import phase2.trade.user.User;

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
        // address book + if user didnt input address
        // home.setText("Location: " + user.getAddressBook().getSelectedAddress().getCity() + ", " + user.getAddress().getCountry());
        bio.setText("Bio: ");
        currentStatus.setText("Current Status: " + user.getUid());
    }
}
