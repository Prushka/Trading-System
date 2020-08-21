package phase2.trade.controller.side;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import phase2.trade.alert.AlertWindow;
import phase2.trade.controller.*;
import phase2.trade.user.controller.LoginController;
import phase2.trade.user.controller.UserSideInfoController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Side menu controller.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "side_menu.fxml")
public class SideMenuController extends AbstractController implements Initializable {

    @FXML
    private JFXListView<SideOption> sideList;

    @FXML
    private JFXListView<SideOption> bottomSideList;

    @FXML
    private Label userInfo, market, wishList, settings, inventory;

    @FXML
    private VBox userInfoBox;

    /**
     * Constructs a new Side menu controller.
     *
     * @param controllerResources the controller resources
     */
    public SideMenuController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    private <T> void loadCenter(ControllerSupplier<T> supplier) {
        getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(supplier));
    }

    /**
     * Sign out.
     */
    public void signOut() {
        AlertWindow alertWindow = getNotificationFactory().confirmWindow("Sign out", "Do you really want to sign out?", "Ok", "Cancel");
        alertWindow.setConfirmHandler(e -> {
            getAccountManager().logOut();
            getSceneManager().switchScene(LoginController::new);
        });
        alertWindow.display();
    }

    /**
     * Exit.
     */
    public void exit() {
        AlertWindow alertWindow = getNotificationFactory().confirmWindow("Exit", "Do you really want to exit?", "Ok", "Cancel");
        alertWindow.setConfirmHandler(e -> {
            Platform.exit();
        });
        alertWindow.display();
    }

    /**
     * {@inheritDoc}
     * The SideOption will have all the information of language, icon path and {@link ControllerSupplier}.<p>
     * This side menu will set its options depending on the User's {@link phase2.trade.permission.PermissionGroup}.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userInfoBox.getChildren().add(getSceneManager().loadPane(UserSideInfoController::new));

        sideList.setCellFactory(param -> new SideListCell(resources));
        bottomSideList.setCellFactory(param -> new SideListCell(resources));
        for (SideOption sideOption : SideOption.values()) {
            if (sideOption.ifDisplay(getAccountManager().getPermissionGroup())) {
                if (sideOption.sidePosition == SideOption.SidePosition.TOP) {
                    sideList.getItems().add(sideOption);
                } else {
                    bottomSideList.getItems().addAll(sideOption);
                }
            }
        }

        sideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.controllerSupplier != null) {
                loadCenter(newValue.controllerSupplier);
            }
        });

        sideList.getSelectionModel().select(3);
        bottomSideList.setOnMouseClicked(event -> {
                    switch (bottomSideList.getSelectionModel().getSelectedItem()) {
                        case SIGN_OUT:
                            signOut();
                            break;
                        case EXIT:
                            exit();
                            break;
                    }
                }
        );

    }
}
