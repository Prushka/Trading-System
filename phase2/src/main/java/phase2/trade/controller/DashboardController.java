package phase2.trade.controller;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "dashboard.fxml")
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
        putPane("topBar", top);
        putPane("centerDashboard", center);
        putPane("rightDashboard", right);
        root.setLeft(getSceneManager().loadPane(SideMenuController::new));

    }

}
