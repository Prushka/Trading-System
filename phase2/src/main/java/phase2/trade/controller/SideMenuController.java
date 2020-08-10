package phase2.trade.controller;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import phase2.trade.inventory.ItemListType;
import phase2.trade.presenter.ControllerSupplier;
import phase2.trade.presenter.ItemListController;
import phase2.trade.presenter.MarketListController;
import phase2.trade.presenter.SceneManager;

import java.net.URL;
import java.util.ResourceBundle;

public class SideMenuController extends AbstractController implements Initializable {

    private VBox right;
    public JFXListView<Label> sideList;
    public Label userInfo, market, wishList, settings, inventory;
    public VBox userInfoBox;

    public JFXListView<Label> bottomSideList;

    public JFXPanel panel = new JFXPanel();
    private VBox center;

    //, VBox center, VBox right
    public SideMenuController(SceneManager sceneManager) {
        super(sceneManager);
    }

    public void setCenter(VBox center) {
        this.center = center;
    }

    // TODO: drop down sub menu

    private <T> void loadSide(String fileName, ControllerSupplier<T> supplier) {
        center.getChildren().clear();
        getSceneManager().addPane(fileName, supplier, center);
    }

    private void inventory() {
        center.getChildren().clear();
        ItemListController controller = new ItemListController(getSceneManager());
        controller.setItemList(getAccountManager().getLoggedInUser().getItemList(ItemListType.INVENTORY));
        getSceneManager().addPane("item_list.fxml", controller, center);
    }

    private void wishList() {
        center.getChildren().clear();
        ItemListController controller = new ItemListController(getSceneManager());
        controller.setItemList(getAccountManager().getLoggedInUser().getItemList(ItemListType.CART));
        getSceneManager().addPane("item_list.fxml", controller, center);
    }

    // TODO: extend for different users
    public void signOut() {
        bottomSideList.getSelectionModel().clearSelection();
        if (getPopupFactory().confirmWindow("Sign out", "Do you really want to sign out?").display()) {
            getAccountManager().logOut();
            getSceneManager().switchScene("login.fxml", LoginController::new);
        } else {
        }
    }

    public void exit() {
        bottomSideList.getSelectionModel().clearSelection();
        if (getPopupFactory().confirmWindow("Exit", "Do you really want to exit?").display()) {
            Platform.exit();
        } else {
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getSceneManager().addPane("user_info_side.fxml", UserInfoPresenter::new, userInfoBox);
        sideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.getId()) {
                    case "userInfo":
                        loadSide("user_info.fxml", UserInfoPresenter::new);
                        break;
                    case "market":
                        loadSide("market_list.fxml", MarketListController::new);
                        break;
                    case "inventory":
                        inventory();
                        break;
                    case "wishList":
                        wishList();
                        break;
                    case "userOperation":
                        loadSide("user_info.fxml", UserOperationController::new);
                        break;
                }
            }
        });

        sideList.getSelectionModel().select(2);
        bottomSideList.setOnMouseClicked(event -> {
                    switch (bottomSideList.getSelectionModel().getSelectedItem().getId()) {
                        case "signOut":
                            signOut();
                            break;
                        case "exit":
                            exit();
                            break;
                    }
                }
        );

    }
}
