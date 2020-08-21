package phase2.trade.controller;

import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import phase2.trade.controller.side.SideMenuController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Dashboard controller.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "dashboard.fxml")
public class DashboardController extends AbstractController implements Initializable {

    /**
     * The Root.
     */
    public BorderPane root;
    /**
     * The Center.
     */
    public VBox center, /**
     * The Right area.
     */
    right, /**
     * The Left area.
     */
    left;
    /**
     * The Top area.
     */
    public HBox top;

    /**
     * Constructs a new Dashboard controller.
     *
     * @param controllerResources the controller resources
     */
    public DashboardController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        putPane(DashboardPane.TOP, top);
        putPane(DashboardPane.CENTER, center);
        putPane(DashboardPane.RIGHT, right);
        putPane(DashboardPane.LEFT, left);
        root.setLeft(getSceneManager().loadPane(SideMenuController::new));
    }

}
