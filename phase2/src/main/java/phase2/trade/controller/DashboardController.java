package phase2.trade.controller;

import com.jfoenix.controls.JFXToolbar;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import phase2.trade.view.CustomWindow;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends AbstractController implements Initializable {

    // public JFXDrawer drawer;
    // public JFXHamburger hamburger;
    public BorderPane root;
    public VBox center, right;
    public HBox top;


    public DashboardController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SideMenuController sideMenuController = new SideMenuController(getControllerResources());
        sideMenuController.loadDashboardElements(center, right, top);
        root.setLeft(getSceneManager().loadPane("side_menu.fxml",
                sideMenuController));

    }

}
