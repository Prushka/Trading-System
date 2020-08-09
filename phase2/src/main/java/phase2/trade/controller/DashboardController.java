package phase2.trade.controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setLeft(loadPane("side_menu.fxml", new SideMenuController(gatewayBundle, accountManager, center, right)));

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
