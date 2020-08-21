package phase2.trade.user.controller;

import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;
import phase2.trade.widget.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The User info controller.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "abstract_masonry.fxml")
public class UserInfoController extends AbstractController implements Initializable {

    @FXML
    private JFXMasonryPane root;

    private final User userToPresent;

    private Parent userWidget, permissionWidget, addressWidget, accountStateWidget, userOptionWidget;


    /**
     * Constructs a new User info controller.
     *
     * @param controllerResources the controller resources
     */
    public UserInfoController(ControllerResources controllerResources) {
        super(controllerResources);
        userToPresent = getAccountManager().getLoggedInUser();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //if (getAccountManager().getLoggedInUser().getPermissionGroup().equals(PermissionGroup.GUEST)) return;

        if (userToPresent == null) return; // this shouldn't happen
        userWidget = getSceneManager().loadPane(UserWidget::new);
        addressWidget = getSceneManager().loadPane(AddressWidget::new);
        accountStateWidget = getSceneManager().loadPane(AccountStateWidget::new);
        permissionWidget = getSceneManager().loadPane(PermissionWidget::new);
        userOptionWidget = getSceneManager().loadPane(UserOptionWidget::new);

        GridPane.setConstraints(userWidget, 1, 1);
        GridPane.setConstraints(permissionWidget, 2, 1);
        GridPane.setConstraints(addressWidget, 3, 1);
        GridPane.setConstraints(accountStateWidget, 4, 1);
        root.setHSpacing(20);
        root.setVSpacing(20);
        root.getChildren().addAll(userWidget, permissionWidget, addressWidget, accountStateWidget, userOptionWidget);

    }
}
