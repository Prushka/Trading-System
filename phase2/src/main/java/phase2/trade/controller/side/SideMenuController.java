package phase2.trade.controller.side;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import phase2.trade.controller.*;
import phase2.trade.controller.market.MarketController;
import phase2.trade.item.controller.CartController;
import phase2.trade.item.controller.InventoryController;
import phase2.trade.controller.market.MarketListController;
import phase2.trade.item.controller.ItemManageController;
import phase2.trade.support.controller.SupportTicketAdminController;
import phase2.trade.support.controller.SupportTicketUserController;
import phase2.trade.user.controller.*;
import phase2.trade.inventory.ItemListType;
import phase2.trade.view.SideListCell;

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

    private <T> void loadCenter(ControllerSupplier<T> supplier) {
        getPane("centerDashboard").getChildren().clear();
        getPane("centerDashboard").getChildren().add(getSceneManager().loadPane(supplier));
    }

    private void loadCenter(Object controller) {
        getPane("centerDashboard").getChildren().clear();
        getPane("centerDashboard").getChildren().add(getSceneManager().loadPane(controller));
    }

    private void inventory() {
        getPane("centerDashboard").getChildren().clear();
        InventoryController controller = new InventoryController(getControllerResources(), ItemListType.INVENTORY);
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
        userInfoBox.getChildren().add(getSceneManager().loadPane(UserSideInfoController::new));

        sideList.setCellFactory(param -> new SideListCell());
        bottomSideList.setCellFactory(param -> new SideListCell());

        switch (getAccountManager().getPermissionGroup()) {
            case REGULAR:
                sideList.setItems(FXCollections.observableArrayList("side.user.info", "side.market", "side.inventory", "side.cart", "side.order","side.user.support"));
                break;
            case ADMIN:
            case HEAD_ADMIN:
                sideList.setItems(FXCollections.observableArrayList("side.user.info", "side.m.users","side.m.items", "side.m.user.ops","side.a.support"));
                break;
            case GUEST:
                sideList.setItems(FXCollections.observableArrayList("side.user.info", "side.market"));
                break;
        }


        bottomSideList.setItems(FXCollections.observableArrayList("Exit", "Sign Out"));
        sideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue) {
                    case "side.user.info":
                        UserInfoController userInfoController = new UserInfoController(getControllerResources());
                        loadCenter(userInfoController);
                        break;
                    case "side.market":
                        // getPane("centerDashboard").getChildren().clear();
                        // getPane("centerDashboard").getChildren().add(getSceneManager().loadPane(new MarketListController(getControllerResources())));
                        loadCenter(MarketController::new);
                        break;
                    case "side.inventory":
                        inventory();
                        break;
                    case "side.cart":
                        cart();
                        break;
                    case "side.m.user.ops":
                        loadCenter(UserOperationController::new);
                        break;
                    case "side.m.users":
                        loadCenter(UserManageController::new);
                        break;
                    case "side.m.items":
                        loadCenter(ItemManageController::new);
                        break;
                    case "side.order":

                        break;
                    case "side.user.support":

                        loadCenter(SupportTicketUserController::new);
                        break;
                    case "side.a.support":

                        loadCenter(SupportTicketAdminController::new);
                        break;
                }
            }
        });

        sideList.getSelectionModel().select(0);
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
