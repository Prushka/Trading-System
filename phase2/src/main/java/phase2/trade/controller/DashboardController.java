package phase2.trade.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import phase2.trade.presenter.SceneManager;
import phase2.trade.view.ConfirmWindow;
import phase2.trade.view.CustomWindow;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends AbstractController implements Initializable {

    // public JFXDrawer drawer;
    // public JFXHamburger hamburger;
    public BorderPane root;
    public VBox center;
    public VBox right;


    public DashboardController(SceneManager sceneManager) {
        super(sceneManager);
    }


    public void signOut(ActionEvent actionEvent) {
        CustomWindow<Boolean> confirmWindow = new ConfirmWindow((Stage) center.getScene().getWindow(), "Sign out", "Do you really want to sign out?");
        if (confirmWindow.display()) {
            getAccountManager().logOut();
            getSceneManager().switchScene("login.fxml", LoginController::new);
        } else {
            // sideList.getSelectionModel().select(old);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SideMenuController sideMenuController = new SideMenuController(getSceneManager());
        sideMenuController.setCenter(center);
        root.setLeft(getSceneManager().loadPane("side_menu_" + getAccountManager().getPermissionGroup().name().toLowerCase() + ".fxml",
                sideMenuController));

    }

}
