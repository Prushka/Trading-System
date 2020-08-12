package phase2.trade.controller;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import phase2.trade.inventory.ItemListType;
import phase2.trade.presenter.ControllerSupplier;
import phase2.trade.presenter.ItemListController;
import phase2.trade.presenter.MarketListController;

import java.net.URL;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "side_menu.fxml")
public class SideMenuController extends AbstractController implements Initializable {

    private VBox center, right;
    private HBox top;

    public JFXListView<String> sideList, bottomSideList;
    public Label userInfo, market, wishList, settings, inventory;
    public VBox userInfoBox;

    public SideMenuController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    public void loadDashboardElements(VBox center, VBox right, HBox top) {
        this.center = center;
        this.right = right;
        this.top = top;
    }

    // TODO: drop down sub menu

    private <T> void loadCenter(String fileName, ControllerSupplier<T> supplier) {
        center.getChildren().clear();
        center.getChildren().add(getSceneManager().loadPane(fileName, supplier));
    }

    private void inventory() {
        center.getChildren().clear();
        ItemListController controller = new ItemListController(getControllerResources(), ItemListType.INVENTORY);
        center.getChildren().add(getSceneManager().loadPane(controller));
    }

    private void wishList() {
        center.getChildren().clear();
        ItemListController controller = new ItemListController(getControllerResources(), ItemListType.INVENTORY);
        center.getChildren().add(getSceneManager().loadPane(controller));
    }

    // TODO: extend for different users
    public void signOut() {
        bottomSideList.getSelectionModel().clearSelection();
        if (getPopupFactory().confirmWindow("Sign out", "Do you really want to sign out?").display()) {
            getAccountManager().logOut();
            getSceneManager().switchScene(LoginController::new);
        }
    }

    public void exit() {
        bottomSideList.getSelectionModel().clearSelection();
        if (getPopupFactory().confirmWindow("Exit", "Do you really want to exit?").display()) {
            Platform.exit();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userInfoBox.getChildren().add(getSceneManager().loadPane(UserInfoPresenter::new));

        sideList.setItems(FXCollections.observableArrayList("userInfo", "market", "inventory", "wishlist"));


        bottomSideList.setItems(FXCollections.observableArrayList("Exit", "Sign Out"));
        sideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue) {
                    case "userInfo":
                        loadCenter("user_info.fxml", UserInfoPresenter::new);
                        break;
                    case "market":
                        center.getChildren().clear();
                        center.getChildren().add(getSceneManager().loadPane(new MarketListController(getControllerResources(), top)));
                        break;
                    case "inventory":
                        inventory();
                        break;
                    case "wishList":
                        wishList();
                        break;
                    case "userOperation":
                        loadCenter("user_info.fxml", UserOperationController::new);
                        break;
                }
            }
        });

        sideList.getSelectionModel().select(1);
        bottomSideList.setOnMouseClicked(event -> {
                    switch (bottomSideList.getSelectionModel().getSelectedItem()) {
                        case "Sign Out":
                            signOut();
                            break;
                        case "Exit":
                            exit();
                            break;
                    }
                }
        );

    }
}
