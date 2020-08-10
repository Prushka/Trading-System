package phase2.trade.controller;

import com.jfoenix.controls.JFXListView;
import javafx.embed.swing.JFXPanel;
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

import java.net.URL;
import java.util.ResourceBundle;

public class AdminSideMenuController extends AbstractController implements Initializable {

    private final VBox right;
    public JFXListView<Label> sideList;
    public Label accounts, userLimits, home;
    public VBox userInfoBox;

    public JFXPanel panel = new JFXPanel();
    private final VBox center;

    private final AccountManager accountManager;

    public AdminSideMenuController(GatewayBundle gatewayBundle, AccountManager accountManager, VBox center, VBox right) {
        super(gatewayBundle);
        this.accountManager = accountManager;
        this.center = center;
        this.right = right;
    }

    private void accounts() {
        Parent userPane = getSceneFactory().loadPane("user_accounts.fxml", new UserAccountsController(gatewayBundle));
        GridPane.setConstraints(userPane, 0, 0);
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    private void userLimits() {
        Parent userPane = getSceneFactory().loadPane("limits.fxml", new LimitsController(gatewayBundle));
        userPane.setPickOnBounds(false);
        GridPane.setConstraints(userPane, 0, 0);
        center.getChildren().clear();
        center.getChildren().addAll(userPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userInfoBox.getChildren().add(getSceneFactory().loadPane("user_info_side.fxml", new UserInfoPresenter(accountManager.getLoggedInUser())));
        sideList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.getId()) {
                    case "accounts":
                        accounts();
                        break;
                    case "userLimits":
                        userLimits();
                        break;
                }
            }
        });
        sideList.getSelectionModel().select(accounts);
    }
}
