package phase2.trade.trade.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import phase2.trade.controller.ControllerResources;
import phase2.trade.trade.TradeOrder;
import phase2.trade.user.User;
import phase2.trade.widget.TradeAddressWidget;
import phase2.trade.widget.TradeConfirmWidget;
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
        User leftSelected, rightSelected;

        WidgetBundle() {

        }

        WidgetBundle(User leftSelected, User rightSelected) {
            tradeTimeWidget = new TradeTimeWidget(getControllerResources(), leftSelected, rightSelected);
            tradeAddressWidget = new TradeAddressWidget(getControllerResources(), leftSelected, rightSelected);
        }

        void loadOnto(Pane pane) {
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

}
