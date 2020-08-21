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

/**
 * The Trade info controller.
 *
 * @author Dan Lyu
 */
public abstract class TradeInfoController extends TradeController {

    /**
     * The Users involved.
     */
    Collection<User> usersInvolved;

    /**
     * The Widget bundle map.
     */
    final Map<TradeOrder, WidgetBundle> widgetBundleMap = new HashMap<>();

    /**
     * The Widget bundle.
     *
     * @author Dan Lyu
     */
    class WidgetBundle {
        /**
         * The Trade time widget.
         */
        TradeTimeWidget tradeTimeWidget;
        /**
         * The Trade address widget.
         */
        TradeAddressWidget tradeAddressWidget;
        /**
         * The Trade confirm widget.
         */
        TradeConfirmWidget tradeConfirmWidget;
        /**
         * The Trade order.
         */
        TradeOrder tradeOrder;

        /**
         * Constructs a new Widget bundle.
         *
         * @param tradeOrder the trade order
         */
        WidgetBundle(TradeOrder tradeOrder) {
            tradeTimeWidget = new TradeTimeWidget(getControllerResources(), tradeOrder);
            tradeAddressWidget = new TradeAddressWidget(getControllerResources(), tradeOrder);
            this.tradeOrder = tradeOrder;
        }

        /**
         * Load onto.
         *
         * @param pane the pane
         */
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

    /**
     * Constructs a new Trade info controller.
     *
     * @param controllerResources the controller resources
     */
    public TradeInfoController(ControllerResources controllerResources) {
        super(controllerResources);
    }


    /**
     * Gets users besides.
     *
     * @param user the user
     * @return the users besides
     */
    ObservableList<User> getUsersBesides(User user) {
        ObservableList<User> list = FXCollections.observableArrayList(usersInvolved);
        list.remove(user);
        return list;
    }

    /**
     * Load combo boxes with users.
     */
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

    /**
     * Refresh table area.
     *
     * @param leftSelected  the left selected
     * @param rightSelected the right selected
     */
    abstract void refreshTableArea(User leftSelected, User rightSelected);

    /**
     * Gets presenting trade.
     *
     * @return the presenting trade
     */
    abstract Trade getPresentingTrade();

    /**
     * Gets user tables.
     *
     * @param matchesWhom the matches whom
     * @return the user tables
     */
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
