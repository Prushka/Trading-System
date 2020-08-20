package phase2.trade.widget;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import phase2.trade.controller.ControllerResources;
import phase2.trade.trade.OrderState;
import phase2.trade.trade.TradeOrder;

import java.net.URL;
import java.util.ResourceBundle;

public class TradeConfirmWidget extends TradeDetailWidget<TradeConfirmWidget.ConfirmationPair> {

    private final CheckBox doIConfirm;
    private final CheckBox doIConfirmTransaction;

    public static class ConfirmationPair {
        public boolean tradeConfirm, transactionConfirm;

        public ConfirmationPair(boolean tradeConfirm, boolean transactionConfirm) {
            this.tradeConfirm = tradeConfirm;
            this.transactionConfirm = transactionConfirm;
        }
    }


    public TradeConfirmWidget(ControllerResources controllerResources, TradeOrder tradeOrder, ConfirmationPair previousValue) {
        super(controllerResources, tradeOrder);
        doIConfirm = new JFXCheckBox("Confirm the trade");
        doIConfirmTransaction = new JFXCheckBox("Completed");
        doIConfirm.setSelected(previousValue.tradeConfirm);
        doIConfirmTransaction.setSelected(previousValue.transactionConfirm);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-a");
        addTitle(doIConfirm, doIConfirmTransaction);
        if (tradeOrder.getOrderState().equals(OrderState.PENDING_TRADE)) {
            doIConfirm.setDisable(true);
        } else {
            doIConfirmTransaction.setDisable(true);
        }
        addContent(new Label("As user: " + getAccountManager().getLoggedInUser().getName()), new Label(tradeOrder.getOrderState().name()));
        refresh();
    }

    public ConfirmationPair getValue() {
        return new ConfirmationPair(doIConfirm.isSelected(), doIConfirmTransaction.isSelected());
    }
}
