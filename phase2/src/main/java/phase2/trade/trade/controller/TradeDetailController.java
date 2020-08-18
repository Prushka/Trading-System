package phase2.trade.trade.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.DashboardPane;
import phase2.trade.item.Item;
import phase2.trade.item.controller.CartController;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.command.CreateTradeCommand;
import phase2.trade.trade.use.CreateTrade;
import phase2.trade.user.User;
import phase2.trade.widget.TradeOptionWidget;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class TradeDetailController extends TradeInfoController {

    private final Map<User, Collection<Item>> usersToItemsToGet;

    private final Map<User, Map<User, UserTable>> userTablesCombination = new HashMap<>();

    private final CreateTrade createTrade;

    private TradeOptionWidget tradeOptionWidget;

    public TradeDetailController(ControllerResources controllerResources, Map<User, Collection<Item>> usersToItemsToGet) {
        super(controllerResources);
        this.usersToItemsToGet = usersToItemsToGet;

        createTrade = new CreateTrade(usersToItemsToGet);

    }

    //dashboard-table-view
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        createTrade.createOrder();

        for (Map.Entry<User, Collection<Item>> entry : usersToItemsToGet.entrySet()) {
            userTablesCombination.put(entry.getKey(), getUserTables(entry.getKey()));
        }

        usersInvolved = createTrade.getUsersInvolved();
        loadComboBoxesWithUsers();

        Label offersFollowingItemsA = new Label("  offers following items");
        Label offersFollowingItemsB = new Label("  offers following items");
        topLeftHBox.getChildren().addAll(leftComboBox, offersFollowingItemsA);
        topRightHBox.getChildren().addAll(rightComboBox, offersFollowingItemsB);

        TradeOptionWidget tradeOptionWidget = getControllerFactory().getController(TradeOptionWidget::new);


        EventHandler<ActionEvent> handler = e -> {
            CreateTradeCommand createTradeCommand = getCommandFactory().getCommand(
                    CreateTradeCommand::new, c -> c.setToUpdate(createTrade.getTrade()));
            for (Map.Entry<TradeOrder, WidgetBundle> entry : widgetBundleMap.entrySet()) {
                entry.getKey().setAddressTrade(entry.getValue().tradeAddressWidget.getValue());
                entry.getKey().setDateAndTime(entry.getValue().tradeTimeWidget.getValue());
            }
            createTradeCommand.execute((result, status) -> {
                status.setSucceeded(() -> {
                    getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(CartController::new));
                    getNotificationFactory().toast("Order successfully placed!");
                });
                status.handle(getNotificationFactory());
            });
        };
        tradeOptionWidget.setEventHandler(handler);
        globalPane.getChildren().addAll(getSceneManager().loadPane(tradeOptionWidget));
        globalPane.setVisible(false);
    }

    private WidgetBundle createWidgetBundleIfNotExist(User leftSelected, User rightSelected) {
        TradeOrder order = createTrade.getTrade().findOrderByUserPair(leftSelected, rightSelected);
        if (order == null) return null;
        if (!widgetBundleMap.containsKey(order)) {
            widgetBundleMap.put(order, new WidgetBundle(leftSelected, rightSelected));
        }
        return widgetBundleMap.get(order);
    }

    @Override
    void refreshTableArea(User leftSelected, User rightSelected) {
        if (leftSelected == null || rightSelected == null) return;
        TableView<Item> left = userTablesCombination.get(rightSelected)
                .get(leftSelected).tableViewGenerator.getTableView();
        TableView<Item> right = userTablesCombination.get(leftSelected)
                .get(rightSelected).tableViewGenerator.getTableView();
        globalPane.setVisible(true);
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
        for (TradeOrder order : createTrade.getTrade().getOrders()) {
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