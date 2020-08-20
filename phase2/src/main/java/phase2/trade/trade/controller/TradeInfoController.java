package phase2.trade.trade.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import phase2.trade.controller.ControllerResources;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.user.User;
import phase2.trade.widget.TradeAddressWidget;
import phase2.trade.widget.TradeConfirmWidget;
import phase2.trade.widget.TradeEmptyWidget;
import phase2.trade.widget.TradeTimeWidget;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class TradeInfoController extends TradeController {

    Collection<User> usersInvolved;

    final Map<TradeOrder, WidgetBundle> widgetBundleMap = new HashMap<>();

    class WidgetBundle {
        TradeTimeWidget tradeTimeWidget;
        TradeAddressWidget tradeAddressWidget;
        TradeConfirmWidget tradeConfirmWidget;
        TradeOrder tradeOrder;

        WidgetBundle(TradeOrder tradeOrder) {
            tradeTimeWidget = new TradeTimeWidget(getControllerResources(), tradeOrder);
            tradeAddressWidget = new TradeAddressWidget(getControllerResources(), tradeOrder);
            this.tradeOrder = tradeOrder;
        }

        void loadOnto(Pane pane) {
            if (tradeOrder.getLeftBundle().getTradeItemHolder().size() == 0 && tradeOrder.getRightBundle().getTradeItemHolder().size() == 0) {
                // No trade will happen between these two people
                // They were not removed from the ComboBoxes because these two users have ongoing order with other participants.
                // All other users who are not involved but have their items chosen in the cart won't appear in ComboBoxes
                pane.getChildren().setAll(getSceneManager().loadPane(
                        new TradeEmptyWidget(getControllerResources(), "Placeholder Trade",
                                "The information won't be saved")
                ));
                return;
            }
            pane.getChildren().setAll(getSceneManager().loadPane(tradeTimeWidget), getSceneManager().loadPane(tradeAddressWidget));
        }
    }

    public TradeInfoController(ControllerResources controllerResources) {
        super(controllerResources);
    }


    ObservableList<User> getUsersBesides(User user) {
        ObservableList<User> list = FXCollections.observableArrayList(usersInvolved);
        list.remove(user);
        return list;
    }

    void loadComboBoxesWithUsers() {

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
    }

    abstract void refreshTableArea(User leftSelected, User rightSelected);

    abstract Trade getPresentingTrade();

    // well these loops need optimization
    Map<User, UserTable> getUserTables(User matchesWhom) {
        Map<User, UserTable> table = new HashMap<>();
        for (TradeOrder order : getPresentingTrade().getOrders()) {
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
