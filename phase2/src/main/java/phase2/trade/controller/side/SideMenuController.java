package phase2.trade.controller.side;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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
import phase2.trade.view.BottomSideListCell;
import phase2.trade.view.SideListCell;

import java.net.URL;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "side_menu.fxml")
public class SideMenuController extends AbstractController implements Initializable {

    @FXML
    private JFXListView<SideOption> sideList;

    @FXML
    private JFXListView<BottomSideOption> bottomSideList;

    @FXML
    private Label userInfo, market, wishList, settings, inventory;

    @FXML
    private VBox userInfoBox;

    public SideMenuController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    // TODO: drop down sub menu

    private <T> void loadCenter(String fileName, ControllerSupplier<T> supplier) {
        getPane(DashboardPane.CENTER).getChildren().clear();
        getPane(DashboardPane.CENTER).getChildren().add(getSceneManager().loadPane(fileName, supplier));
    }

    private <T> void loadCenter(ControllerSupplier<T> supplier) {
        getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(supplier));
    }

    private void loadCenter(Object controller) {
        getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(controller));
    }

    private void inventory() {
        InventoryController controller = new InventoryController(getControllerResources(), ItemListType.INVENTORY);
        getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(controller));
    }

    private void cart() {
        CartController controller = new CartController(getControllerResources(), ItemListType.CART);
        getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(controller));
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
        bottomSideList.setCellFactory(param -> new BottomSideListCell());
        for (SideOption sideOption : SideOption.values()) {
            if (sideOption.ifDisplay(getAccountManager().getPermissionGroup())) {
                sideList.getItems().add(sideOption);
            }
        }
        for (BottomSideOption bottomSide : BottomSideOption.values()) {
            if (bottomSide.ifDisplay(getAccountManager().getPermissionGroup())) {
                bottomSideList.getItems().add(bottomSide);
            }
        }
        sideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue) {
                    case USER:
                        UserInfoController userInfoController = new UserInfoController(getControllerResources());
                        loadCenter(userInfoController);
                        break;
                    case MARKET:
                        getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(new MarketListController(getControllerResources())));
                        break;
                    case INVENTORY:
                        inventory();
                        break;
                    case CART:
                        // cart();
                        loadCenter(MarketController::new);
                        break;
                    case MANAGE_USERS_OPERATIONS:
                        loadCenter(UserOperationController::new);
                        break;
                    case MANAGE_USERS:
                        loadCenter(UserManageController::new);
                        break;
                    case MANAGE_ITEMS:
                        loadCenter(ItemManageController::new);
                        break;
                    case ORDER:

                        break;
                    case SUPPORT:

                        loadCenter(SupportTicketUserController::new);
                        break;
                    case MANAGE_SUPPORT:

                        loadCenter(SupportTicketAdminController::new);
                        break;
                }
            }
        });

        sideList.getSelectionModel().select(0);
        bottomSideList.setOnMouseClicked(event -> {
                    switch (bottomSideList.getSelectionModel().getSelectedItem()) {
                        case SIGN_OUT:
                            signOut();
                            break;
                        case EXIT:
                            exit();
                            break;
                    }
                }
        );

    }
}
