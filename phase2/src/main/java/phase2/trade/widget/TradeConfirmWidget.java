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
    private final CheckBox doIConfirmTransactionBack;

    public static class ConfirmationPair {
        public boolean tradeConfirm, transactionConfirm, transactionBackConfirm;

        public ConfirmationPair(boolean tradeConfirm, boolean transactionConfirm, boolean transactionBackConfirm) {
            this.tradeConfirm = tradeConfirm;
            this.transactionConfirm = transactionConfirm;
            this.transactionBackConfirm = transactionBackConfirm;
        }
    }


    public TradeConfirmWidget(ControllerResources controllerResources, TradeOrder tradeOrder, ConfirmationPair previousValue) {
        super(controllerResources, tradeOrder);
        doIConfirm = new JFXCheckBox("Confirm the trade");
        doIConfirmTransaction = new JFXCheckBox("Completed");
        doIConfirmTransactionBack = new JFXCheckBox("TradeBack Completed");
        doIConfirm.setSelected(previousValue.tradeConfirm);
        doIConfirmTransaction.setSelected(previousValue.transactionConfirm);
        doIConfirmTransactionBack.setSelected(previousValue.transactionBackConfirm);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-a");
        addContent(doIConfirm, doIConfirmTransaction, doIConfirmTransactionBack);
        if (!tradeOrder.getOrderState().equals(OrderState.PENDING_CONFIRMATION)) {
            doIConfirm.setDisable(true);
        }
        if (!tradeOrder.getOrderState().equals(OrderState.PENDING_TRADE)) {
            doIConfirmTransaction.setDisable(true);
        }
        if (!tradeOrder.getOrderState().equals(OrderState.PENDING_TRADE_BACK)) {
            doIConfirmTransactionBack.setDisable(true);
        }

        addContent(new Label("As user: " + getAccountManager().getLoggedInUser().getName()), new Label(tradeOrder.getOrderState().name()));
        refresh();
    }

    public ConfirmationPair getValue() {
        return new ConfirmationPair(doIConfirm.isSelected(), doIConfirmTransaction.isSelected(), doIConfirmTransactionBack.isSelected());
    }
}
