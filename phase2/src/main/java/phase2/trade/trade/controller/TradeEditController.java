package phase2.trade.trade.controller;

import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import phase2.trade.address.Address;
import phase2.trade.alert.AlertWindow;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.refresh.ReType;
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

    private final EditTrade editTrade;

    private final AlertWindow alertWindow;

    class EditWidgetBundle extends WidgetBundle {

        EditWidgetBundle(TradeOrder tradeOrder) {
            super(tradeOrder);
        }

        void setTradeConfirm(Boolean previousTradeValue, Boolean previousTransactionValue, boolean previousTransactionBackValue) {
            tradeConfirmWidget = new TradeConfirmWidget(getControllerResources(), tradeOrder, new TradeConfirmWidget.ConfirmationPair(
                    previousTradeValue, previousTransactionValue, previousTransactionBackValue));
        }

        void setTradeAddress(Address previousValue) {
            tradeAddressWidget = new TradeAddressWidget(getControllerResources(), tradeOrder, previousValue);
        }

        void setTradeTime(LocalDateTime previousValue) {
            tradeTimeWidget = new TradeTimeWidget(getControllerResources(), tradeOrder, previousValue);
        }

        void loadOnto(Pane pane) {
            if (!(tradeOrder.ifUserInOrder(getAccountManager().getLoggedInUser()))) { // this is not the (logged in user)'s order, display nothing
                pane.getChildren().setAll(getSceneManager().loadPane(new TradeEmptyWidget(getControllerResources(), "Not Your Order")));
                return;
            }
            if (!tradeOrder.getOrderState().editable) {
                pane.getChildren().setAll(getSceneManager().loadPane(new TradeEmptyWidget(getControllerResources(), "Uneditable Trade", "State: " + tradeOrder.getOrderState())));

                return;
            }
            super.loadOnto(pane);
            pane.getChildren().add(getSceneManager().loadPane(tradeConfirmWidget));
        }
    }

    public TradeEditController(ControllerResources controllerResources, Trade trade, AlertWindow alertWindow) {
        super(controllerResources);
        this.trade = trade;
        editTrade = new EditTrade(trade, getAccountManager().getLoggedInUser(), getConfigBundle().getTradeConfig());
        this.alertWindow = alertWindow;
    }

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

            Boolean success = true;

            for (Map.Entry<TradeOrder, WidgetBundle> entry : widgetBundleMap.entrySet()) {
                UserOrderBundle loggedIn = entry.getKey().findBundleByUser(getAccountManager().getLoggedInUser());
                if (loggedIn != null) {
                    editTrade.editConfirm(entry.getKey(), entry.getValue().tradeConfirmWidget.getValue());
                }
                success = editTrade.editAddress(entry.getKey(), entry.getValue().tradeAddressWidget.getValue()).getResultState().handle(getNotificationFactory(), success);
                success = editTrade.editTime(entry.getKey(), entry.getValue().tradeTimeWidget.getValue()).getResultState().handle(getNotificationFactory(), success);
            }
            Boolean finalSuccess = success;
            editTradeCommand.execute((result, status) -> {
                status.setSucceeded(() -> {
                    if (finalSuccess) {
                        getNotificationFactory().toast(2, "Order successfully updated!");
                    }
                    publish(ReType.REFRESH, TradeListController.class);
                });
                status.handle(getNotificationFactory());
            });
        });
    }

    private WidgetBundle createWidgetBundleIfNotExist(User leftSelected, User rightSelected) {
        TradeOrder order = trade.findOrderByUserPair(leftSelected, rightSelected);
        if (order == null) return null;
        if (!widgetBundleMap.containsKey(order)) {
            EditWidgetBundle editWidgetBundle = new EditWidgetBundle(order);
            if (order.ifUserInOrder(getAccountManager().getLoggedInUser())) {
                editWidgetBundle.setTradeAddress(order.getAddressTrade());
                editWidgetBundle.setTradeTime(order.getDateAndTime());
                UserOrderBundle logged = order.findBundleByUser(getAccountManager().getLoggedInUser());
                editWidgetBundle.setTradeConfirm(logged.isTradeConfirmed(),
                        logged.isTransactionConfirmed(), logged.isTransactionBackConfirmed());
            }
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

    @Override
    Trade getPresentingTrade() {
        return trade;
    }
}
