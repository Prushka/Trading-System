package phase2.trade.controller;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import phase2.trade.view.SceneFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

public class DashboardController implements Initializable {

    public JFXDrawer drawer;
    public JFXHamburger hamburger;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StackPane side = null;
        try {
            side = new SceneFactory().getLoader("side_menu.fxml").load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        drawer.setSidePane(side);

        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);

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
