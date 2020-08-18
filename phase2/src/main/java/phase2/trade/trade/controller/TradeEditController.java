package phase2.trade.trade.controller;

import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import phase2.trade.address.Address;
import phase2.trade.alert.AlertWindow;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.trade.OrderState;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.trade.command.EditTradeCommand;
import phase2.trade.trade.use.EditTrade;
import phase2.trade.user.User;
import phase2.trade.widget.TradeAddressWidget;
import phase2.trade.widget.TradeConfirmWidget;
import phase2.trade.widget.TradeEmptyWidget;
import phase2.trade.widget.TradeTimeWidget;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;

public class TradeEditController extends TradeInfoController {

    private final Map<User, Map<User, UserTable>> userTablesCombination = new HashMap<>();

    private final Trade trade;

    private final EditWidgetBundle placeHolder;

    private final EditTrade editTrade;

    private final AlertWindow alertWindow;

    class EditWidgetBundle extends WidgetBundle {

        TradeEmptyWidget tradeEmptyWidget;

        EditWidgetBundle(User leftSelected, User rightSelected) {
            this.leftSelected = leftSelected;
            this.rightSelected = rightSelected;
            tradeEmptyWidget = new TradeEmptyWidget(getControllerResources(), leftSelected, rightSelected);
        }

        void setTradeConfirm(Boolean previousValue) {
            tradeConfirmWidget = new TradeConfirmWidget(getControllerResources(), leftSelected, rightSelected, previousValue);
        }

        void setTradeAddress(Address previousValue) {
            tradeAddressWidget = new TradeAddressWidget(getControllerResources(), leftSelected, rightSelected, previousValue);
        }

        void setTradeTime(LocalDateTime previousValue) {
            tradeTimeWidget = new TradeTimeWidget(getControllerResources(), leftSelected, rightSelected, previousValue);
        }

        void loadOnto(Pane pane, OrderState orderState) {
            if ((leftSelected == null && rightSelected == null)) {
                pane.getChildren().setAll(getSceneManager().loadPane(tradeEmptyWidget));
                return;
            }
            if (!orderState.editable) {
                tradeEmptyWidget.setText("This is an uneditable Trade");
                pane.getChildren().setAll(getSceneManager().loadPane(tradeEmptyWidget));
                return;
            }
            loadOnto(pane);
            pane.getChildren().add(getSceneManager().loadPane(tradeConfirmWidget));
        }
    }

    public TradeEditController(ControllerResources controllerResources, Trade trade, AlertWindow alertWindow) {
        super(controllerResources);
        this.trade = trade;
        editTrade = new EditTrade(trade, getAccountManager().getLoggedInUser(), getConfigBundle().getTradeConfig());
        placeHolder = new EditWidgetBundle(null, null);
        this.alertWindow = alertWindow;
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

        loadComboBoxesWithUsers();

        Label offersFollowingItemsA = new Label("  offers following items");
        Label offersFollowingItemsB = new Label("  offers following items");
        topLeftHBox.getChildren().addAll(leftComboBox, offersFollowingItemsA);
        topRightHBox.getChildren().addAll(rightComboBox, offersFollowingItemsB);

        alertWindow.setConfirmHandler(e -> {
            EditTradeCommand editTradeCommand = getCommandFactory().getCommand(
                    EditTradeCommand::new, c -> c.setToUpdate(trade));

            for (Map.Entry<TradeOrder, WidgetBundle> entry : widgetBundleMap.entrySet()) {

                UserOrderBundle loggedIn = entry.getKey().findBundleByUser(getAccountManager().getLoggedInUser());

                if (loggedIn != null) {
                    editTrade.editConfirm(entry.getKey(), entry.getValue().tradeConfirmWidget.getValue(), resultStatus -> {
                        resultStatus.handle(getNotificationFactory());
                    });
                }

                editTrade.editAddress(entry.getKey(), entry.getValue().tradeAddressWidget.getValue(), resultStatus -> {
                    resultStatus.handle(getNotificationFactory());
                });

                editTrade.editTime(entry.getKey(), entry.getValue().tradeTimeWidget.getValue(), resultStatus -> {
                    resultStatus.handle(getNotificationFactory());
                });

            }
            editTradeCommand.execute((result, status) -> {
                status.setSucceeded(() -> {
                    //getNotificationFactory().toast("Order successfully updated!");
                });
                status.handle(getNotificationFactory());
            });
        });
    }

    private WidgetBundle createWidgetBundleIfNotExist(User leftSelected, User rightSelected) {
        TradeOrder order = trade.findOrderByUserPair(leftSelected, rightSelected);
        if (order == null) return null;
        if (!widgetBundleMap.containsKey(order)) {
            if (order.findBundleByUser(getAccountManager().getLoggedInUser()) == null) { // this is not the (logged in user)'s order, display nothing
                return placeHolder;
            }
            EditWidgetBundle editWidgetBundle = new EditWidgetBundle(leftSelected, rightSelected);
            editWidgetBundle.setTradeAddress(order.getAddressTrade());
            editWidgetBundle.setTradeTime(order.getDateAndTime());
            editWidgetBundle.setTradeConfirm(order.findBundleByUser(getAccountManager().getLoggedInUser()).hasConfirmed());
            widgetBundleMap.put(order, editWidgetBundle);
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

        leftTableArea.getChildren().setAll(left);
        rightTableArea.getChildren().setAll(right);
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
