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

    public SideMenuController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    // TODO: drop down sub menu

    private <T> void loadCenter(String fileName, ControllerSupplier<T> supplier) {
        getPane(DashboardPane.CENTER).getChildren().clear();
        getPane(DashboardPane.CENTER).getChildren().add(getSceneManager().loadPane(fileName, supplier));
    }

    private <T> void loadCenter(ControllerSupplier<T> supplier) {
        getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(supplier));
    }

    private void loadCenter(Object controller) {
        getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(controller));
    }

    public void signOut() {
        AlertWindow alertWindow = getNotificationFactory().confirmWindow("Sign out", "Do you really want to sign out?", "Ok", "Cancel");
        alertWindow.setConfirmHandler(e -> {
            getAccountManager().logOut();
            getSceneManager().switchScene(LoginController::new);
        });
        alertWindow.display();
    }

    public void exit() {
        AlertWindow alertWindow = getNotificationFactory().confirmWindow("Exit", "Do you really want to exit?", "Ok", "Cancel");
        alertWindow.setConfirmHandler(e -> {
            Platform.exit();
        });
        alertWindow.display();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userInfoBox.getChildren().add(getSceneManager().loadPane(UserSideInfoController::new));

        sideList.setCellFactory(param -> new SideListCell());
        bottomSideList.setCellFactory(param -> new SideListCell());
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
