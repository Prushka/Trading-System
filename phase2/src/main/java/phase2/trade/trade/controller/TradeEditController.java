package phase2.trade.trade.controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.user.User;
import phase2.trade.widget.TradeConfirmWidget;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;

public class TradeEditController extends TradeController {

    private final Map<User, Map<User, UserTable>> userTablesCombination = new HashMap<>();

    private final Map<TradeOrder, EditWidgetBundle> widgetBundleMap = new HashMap<>();

    private final Trade trade;

    class EditWidgetBundle extends WidgetBundle {

        TradeConfirmWidget tradeConfirmWidget;

        public EditWidgetBundle(User leftSelected, User rightSelected) {
            super(leftSelected, rightSelected);
            tradeConfirmWidget = new TradeConfirmWidget(getControllerResources(), leftSelected, rightSelected);
        }

        void loadOnto(Pane pane) {
            super.loadOnto(pane);
            pane.getChildren().add(getSceneManager().loadPane(tradeConfirmWidget));
        }
    }

    public TradeEditController(ControllerResources controllerResources, Trade trade) {
        super(controllerResources);
        this.trade = trade;
    }

    //dashboard-table-view
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usersInvolved = new HashSet<>();
        for (TradeOrder tradeOrder : trade.findOrdersContainingUser(getAccountManager().getLoggedInUser())) {
            userTablesCombination.put(tradeOrder.getLeftUser(), getUserTables(tradeOrder.getLeftUser()));
            userTablesCombination.put(tradeOrder.getRightUser(), getUserTables(tradeOrder.getRightUser()));
            usersInvolved.add(tradeOrder.getLeftUser());
            usersInvolved.add(tradeOrder.getRightUser());
        }

        rightComboBox = getUserComboBox(usersInvolved);
        leftComboBox = getUserComboBox(usersInvolved);
        leftComboBox.setOnAction(e -> {
            User leftSelected = leftComboBox.getSelectionModel().getSelectedItem();
            User rightSelected = rightComboBox.getSelectionModel().getSelectedItem();
            refreshTableArea(leftSelected, rightSelected);
            rightComboBox.setItems(getUsersBesides(leftSelected));
        });
        rightComboBox.setOnAction(e -> {
            User leftSelected = leftComboBox.getSelectionModel().getSelectedItem();
            User rightSelected = rightComboBox.getSelectionModel().getSelectedItem();
            refreshTableArea(leftSelected, rightSelected);
            leftComboBox.setItems(getUsersBesides(rightSelected));
        });

        Label offersFollowingItemsA = new Label("  offers following items");
        Label offersFollowingItemsB = new Label("  offers following items");
        topLeftHBox.getChildren().addAll(leftComboBox, offersFollowingItemsA);
        topRightHBox.getChildren().addAll(rightComboBox, offersFollowingItemsB);

        CheckBox checkBox = new JFXCheckBox("Confirm Trade");


        EventHandler<ActionEvent> handler = e -> {

        };

        globalPane.getChildren().addAll(checkBox);
    }

    private WidgetBundle createWidgetBundleIfNotExist(User leftSelected, User rightSelected) {
        TradeOrder order = trade.findOrderByUserPair(leftSelected, rightSelected);
        if (order == null) return null;
        if (!widgetBundleMap.containsKey(order)) {
            widgetBundleMap.put(order, new EditWidgetBundle(leftSelected, rightSelected));
        }
        return widgetBundleMap.get(order);
    }

    private void refreshTableArea(User leftSelected, User rightSelected) {
        if (leftSelected == null || rightSelected == null) return;
        TableView<Item> left = userTablesCombination.get(rightSelected)
                .get(leftSelected).tableViewGenerator.getTableView();
        TableView<Item> right = userTablesCombination.get(leftSelected)
                .get(rightSelected).tableViewGenerator.getTableView();

        leftTableArea.getChildren().setAll(left);
        rightTableArea.getChildren().setAll(right);
        if (left.getItems().size() == 0 && right.getItems().size() == 0) {
            // No trade will happen between these two people
            // They were not removed from the ComboBoxes because these two users have ongoing order with other participants.
            // All other users who are not involved but have their items chosen in the cart won't appear in ComboBoxes
            getNotificationFactory().toast(13,
                    "Please SKIP these two users since they don't have any items to trade! Their information won't be saved");
        }
        WidgetBundle widgetBundle = createWidgetBundleIfNotExist(leftSelected, rightSelected);
        if (widgetBundle != null) widgetBundle.loadOnto(buttonPane);
    }

    // well these loops need optimization
    private Map<User, UserTable> getUserTables(User matchesWhom) {
        Map<User, UserTable> table = new HashMap<>();
        for (TradeOrder order : trade.getOrders()) {
            if (order.getLeftUser().getUid().equals(matchesWhom.getUid())) {
                UserTable userTable = new UserTable(order.getRightBundle().getUser(),
                        FXCollections.observableArrayList(
                                order.getRightBundle().getTradeItemHolder().getSetOfItems()), getControllerResources());
                table.put(order.getRightBundle().getUser(), userTable);
            }
            if (order.getRightUser().getUid().equals(matchesWhom.getUid())) {
                UserTable userTable = new UserTable(order.getRightBundle().getUser(),
                        FXCollections.observableArrayList(
                                order.getLeftBundle().getTradeItemHolder().getSetOfItems()), getControllerResources());
                table.put(order.getLeftUser(), userTable);
            }
        }
        return table;
    }

}
