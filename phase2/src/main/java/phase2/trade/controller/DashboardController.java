package phase2.trade.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;
import phase2.trade.user.AdministrativeUser;
import phase2.trade.view.ConfirmWindow;
import phase2.trade.view.CustomWindow;

import java.beans.EventHandler;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends AbstractController implements Initializable {

    // public JFXDrawer drawer;
    // public JFXHamburger hamburger;
    public BorderPane root;
    public VBox center;
    public VBox right;


    private final AccountManager accountManager;

    public DashboardController(GatewayBundle gatewayBundle, AccountManager accountManager) {
        super(gatewayBundle);
        this.accountManager = accountManager;
    }


    public void signOut(ActionEvent actionEvent) {
        CustomWindow<Boolean> confirmWindow = new ConfirmWindow((Stage) center.getScene().getWindow(),"Sign out", "Do you really want to sign out?");
        if (confirmWindow.display()) {
            accountManager.logOut();
            getSceneFactory().switchScene("login.fxml", new LoginController(gatewayBundle, accountManager), center);
        } else {
            // sideList.getSelectionModel().select(old);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setLeft(getSceneFactory().loadPane("side_menu_" + accountManager.getPermissionGroup().name().toLowerCase() + ".fxml",
                new SideMenuController(gatewayBundle, accountManager, center, right)));
    }
}
