package phase2.trade.controller;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;

import java.net.URL;
import java.util.ResourceBundle;

public class SideMenuController extends AbstractController implements Initializable {

    public JFXListView<Label> sideList;
    public Label userInfo, market, wishList, settings, logOut, inventory;
    public VBox userInfoBox;

    private GridPane center;

    private final AccountManager accountManager;

    public SideMenuController(GatewayBundle gatewayBundle, AccountManager accountManager, GridPane center) {
        super(gatewayBundle);
        this.accountManager = accountManager;
        this.center = center;
    }

    private void logOut(Label old) {
        ConfirmPopup confirmPopup = new ConfirmPopup();
        // Parent confirm = loadPane("confirm_popup.fxml", confirmPopup);
        if (confirmPopup.display("Log out", "Do you really want to log out?")) {
            accountManager.logOut();
            switchScene("login.fxml", new LoginController(gatewayBundle, accountManager), logOut);
        } else {
            sideList.getSelectionModel().clearAndSelect(1);
            // sideList.getSelectionModel().select(old);
        }
    }

    private void userInfo() {
        Parent userPane = loadPane("user_info.fxml", new UserInfoPresenter(accountManager.getLoggedInUser()));
        GridPane.setConstraints(userPane, 0, 0);
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    private void market() {
        Parent userPane = loadPane("market.fxml", new MarketController(accountManager.getLoggedInUser()));
        GridPane.setConstraints(userPane, 0, 0);
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    private void inventory() {
        Parent userPane = loadPane("add_item.fxml", new ItemAddController());
        userPane.setPickOnBounds(false);
        GridPane.setConstraints(userPane, 0, 0);
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    private void wishList() {
        Parent userPane = loadPane("add_wish.fxml", new WishItemAddController());
        GridPane.setConstraints(userPane, 0, 0);
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userInfoBox.getChildren().add(loadPane("user_info_side.fxml", new UserInfoPresenter(accountManager.getLoggedInUser())));
        sideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.getId()) {
                    case "userInfo":
                        userInfo();
                        break;
                    case "market":
                        market();
                        break;
                    case "inventory":
                        inventory();
                        break;
                    case "wishList":
                        wishList();
                        break;
                    case "logOut":
                        logOut(oldValue);
                        break;
                }
            }
        });
        sideList.getSelectionModel().select(inventory);
    }
}
