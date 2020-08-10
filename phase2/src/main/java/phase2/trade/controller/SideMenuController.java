package phase2.trade.controller;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.presenter.ItemListController;
import phase2.trade.user.AccountManager;
import phase2.trade.user.RegularUser;
import phase2.trade.view.ConfirmWindow;

import java.net.URL;
import java.util.ResourceBundle;

public class SideMenuController extends AbstractController implements Initializable {

    private final VBox right;
    public JFXListView<Label> sideList;
    public Label userInfo, market, wishList, settings, inventory;
    public VBox userInfoBox;

    public JFXListView<Label> bottomSideList;

    public JFXPanel panel = new JFXPanel();
    private final VBox center;

    private final AccountManager accountManager;

    public SideMenuController(GatewayBundle gatewayBundle, AccountManager accountManager, VBox center, VBox right) {
        super(gatewayBundle);
        this.accountManager = accountManager;
        this.center = center;
        this.right = right;
    }

    // TODO: drop down sub menu & exit
    private void userInfo() {
        Parent userPane = getSceneFactory().loadPane("user_info.fxml", new UserInfoPresenter(accountManager.getLoggedInUser()));
        GridPane.setConstraints(userPane, 0, 0);
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    private void market() {
        Parent userPane = getSceneFactory().loadPane("market.fxml", new MarketController(getGatewayBundle(),
                accountManager.getLoggedInUser()));
        GridPane.setConstraints(userPane, 0, 0);
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    private void inventory() {
        Parent userPane = getSceneFactory().loadPane("item_list.fxml", new ItemListController(gatewayBundle, ((RegularUser) accountManager.getLoggedInUser()), ItemListType.INVENTORY));
        userPane.setPickOnBounds(false);
        GridPane.setConstraints(userPane, 0, 0);
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    private void wishList() {
        Parent userPane = getSceneFactory().loadPane("add_wish.fxml", new WishItemAddController(gatewayBundle, ((RegularUser) accountManager.getLoggedInUser()), ItemListType.CART));
        GridPane.setConstraints(userPane, 0, 0);
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    // make a factory for this
    public void signOut() {
        ConfirmWindow confirmWindow = new ConfirmWindow();
        if (confirmWindow.display("Sign out", "Do you really want to sign out?")) {
            accountManager.logOut();
            getSceneFactory().switchScene("login.fxml", new LoginController(gatewayBundle, accountManager), center);
        } else {
            // sideList.getSelectionModel().select(old);
        }
    }

    // make a factory for this
    public void exit() {
        ConfirmWindow confirmWindow = new ConfirmWindow();
        if (confirmWindow.display("Sign out", "Do you really want to exit?")) {
            Platform.exit();
        } else {
            // sideList.getSelectionModel().select(old);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userInfoBox.getChildren().add(getSceneFactory().loadPane("user_info_side.fxml", new UserInfoPresenter(accountManager.getLoggedInUser())));
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
                }
            }
        });

        bottomSideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.getId()) {
                    case "signOut":
                        signOut();
                        break;
                    case "exit":
                        exit();
                        break;
                }
            }
        });
        sideList.getSelectionModel().select(userInfo);
    }
}
