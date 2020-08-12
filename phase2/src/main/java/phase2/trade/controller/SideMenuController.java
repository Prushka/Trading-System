package phase2.trade.controller;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import phase2.trade.inventory.ItemListType;
import phase2.trade.presenter.CartController;
import phase2.trade.presenter.ControllerSupplier;
import phase2.trade.presenter.ItemListController;
import phase2.trade.presenter.MarketListController;

import java.net.URL;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "side_menu.fxml")
public class SideMenuController extends AbstractController implements Initializable {

    public JFXListView<String> sideList, bottomSideList;
    public Label userInfo, market, wishList, settings, inventory;
    public VBox userInfoBox;

    public SideMenuController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    // TODO: drop down sub menu

    private <T> void loadCenter(String fileName, ControllerSupplier<T> supplier) {
        getPane("centerDashboard").getChildren().clear();
        getPane("centerDashboard").getChildren().add(getSceneManager().loadPane(fileName, supplier));
    }

    private void inventory() {
        getPane("centerDashboard").getChildren().clear();
        ItemListController controller = new ItemListController(getControllerResources(), ItemListType.INVENTORY);
        getPane("centerDashboard").getChildren().add(getSceneManager().loadPane(controller));
    }

    private void cart() {
        getPane("centerDashboard").getChildren().clear();
        CartController controller = new CartController(getControllerResources(), ItemListType.CART);
        getPane("centerDashboard").getChildren().add(getSceneManager().loadPane(controller));
    }

    // TODO: extend for different users
    public void signOut() {
        if (getPopupFactory().confirmWindow("Sign out", "Do you really want to sign out?").display()) {
            getAccountManager().logOut();
            getSceneManager().switchScene(LoginController::new);
        }
    }

    public void exit() {
        if (getPopupFactory().confirmWindow("Exit", "Do you really want to exit?").display()) {
            Platform.exit();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userInfoBox.getChildren().add(getSceneManager().loadPane(UserInfoPresenter::new));

        sideList.setItems(FXCollections.observableArrayList("userInfo", "market", "inventory", "cart"));


        bottomSideList.setItems(FXCollections.observableArrayList("Exit", "Sign Out"));
        sideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue) {
                    case "userInfo":
                        loadCenter("user_info.fxml", UserInfoPresenter::new);
                        break;
                    case "market":
                        getPane("centerDashboard").getChildren().clear();
                        getPane("centerDashboard").getChildren().add(getSceneManager().loadPane(new MarketListController(getControllerResources())));
                        break;
                    case "inventory":
                        inventory();
                        break;
                    case "cart":
                        cart();
                        break;
                    case "userOperation":
                        loadCenter("user_info.fxml", UserOperationController::new);
                        break;
                }
            }
        });

        sideList.getSelectionModel().select(2);
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
