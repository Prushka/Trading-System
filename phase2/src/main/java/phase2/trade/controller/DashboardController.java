package phase2.trade.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;
import phase2.trade.user.AdministrativeUser;

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
        ConfirmWindow confirmWindow = new ConfirmWindow();
        if (confirmWindow.display("Sign out", "Do you really want to sign out?")) {
            accountManager.logOut();
            switchScene("login.fxml", new LoginController(gatewayBundle, accountManager), center);
        } else {
            // sideList.getSelectionModel().select(old);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // if (accountManager.getLoggedInUser() instanceof AdministrativeUser){
            // root.setLeft(loadPane("admin_side_menu.fxml", new AdminSideMenuController(gatewayBundle, accountManager, center, right)));
        // } else {
        root.setLeft(loadPane("side_menu.fxml", new SideMenuController(gatewayBundle, accountManager, center, right)));
        // }

        /*
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);

        drawer.setOnDrawerOpening(e -> {
            transition.setRate(1);
            transition.play();
        });

        drawer.setOnDrawerClosing(e -> {
            transition.setRate(-1);
            transition.play();
        });

        drawer.setOnDrawerOpened(e -> {
            top.setPickOnBounds(true);
        });

        drawer.setOnDrawerClosed(e -> {
            top.setPickOnBounds(false);
        });

        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();

            if (drawer.isOpened() || drawer.isOpening())
                drawer.close();
            else
                drawer.open();
        });*/
    }
}
