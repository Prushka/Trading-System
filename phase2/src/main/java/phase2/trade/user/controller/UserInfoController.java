package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import phase2.trade.avatar.Avatar;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.EditableController;
import phase2.trade.editor.UserEditor;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.user.User;
import phase2.trade.user.command.ChangePassword;
import phase2.trade.user.command.ChangeUserName;
import phase2.trade.user.command.UpdateUsers;
import phase2.trade.view.widget.*;
import phase2.trade.view.window.GeneralVBoxAlert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

// TODO: ALL Controllers that don't extend AbstractTable... is a mess
//  Refactor everything in the future
@ControllerProperty(viewFile = "abstract_border.fxml")
public class UserInfoController extends AbstractController implements Initializable {

    @FXML
    private BorderPane root;

    private final User userToPresent;

    private Parent userWidget, permissionWidget, addressWidget, accountStateWidget, userOptionWidget;

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
        HBox top = new HBox(userWidget, permissionWidget, addressWidget, accountStateWidget, userOptionWidget);
        top.setSpacing(20);
        root.setTop(top);
    }
}
