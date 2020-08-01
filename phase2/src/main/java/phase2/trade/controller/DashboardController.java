package phase2.trade.controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.Transition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import phase2.trade.view.SceneFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends AbstractController implements Initializable {

    public JFXDrawer drawer;
    public JFXHamburger hamburger;
    public GridPane center;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        drawer.setSidePane(loadPane("side_menu.fxml", new SideMenuController(center)));

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

        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();

            if (drawer.isOpened() || drawer.isOpening())
                drawer.close();
            else
                drawer.open();
        });
    }
}
