package phase2.trade.user.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import phase2.trade.avatar.UploadAvatarController;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The User side info controller.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "user_info_side.fxml")
public class UserSideInfoController extends AbstractController implements Initializable {

    /**
     * The User id.
     */
    public Label userId = new Label();
    /**
     * The User name.
     */
    public Label userName = new Label();
    /**
     * The Email.
     */
    public Label email = new Label();
    /**
     * The Home.
     */
    public Label home = new Label();
    /**
     * The Permission group.
     */
    public Label permissionGroup = new Label();
    /**
     * The Image view.
     */
    public ImageView imageView;

    private final User user;

    /**
     * Constructs a new User side info controller.
     *
     * @param controllerResources the controller resources
     */
    public UserSideInfoController(ControllerResources controllerResources) {
        super(controllerResources);
        this.user = getAccountManager().getLoggedInUser();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UploadAvatarController uploadAvatarController = getControllerFactory().getController(UploadAvatarController::new);

        imageView.setOnMouseClicked(event -> uploadAvatarController.uploadAvatar());
        refresh();
    }

    @Override
    public void refresh() {
        userId.setText("User Id: " + user.getUid());
        userName.setText("User Name: " + user.getName());
        email.setText("Email: " + user.getEmail());
        permissionGroup.setText("PermissionGroup: " + user.getPermissionGroup());

        if (user.getAvatar() != null && user.getAvatar().getImageData() != null) {
            Image img = new Image(new ByteArrayInputStream(user.getAvatar().getImageData()));
            imageView.setImage(img);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
        }
        super.refresh();
    }
}
