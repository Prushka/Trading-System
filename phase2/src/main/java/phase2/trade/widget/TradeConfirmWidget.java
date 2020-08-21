package phase2.trade.widget;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import phase2.trade.controller.ControllerResources;
import phase2.trade.trade.OrderState;
import phase2.trade.trade.TradeOrder;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Trade confirm widget.
 *
 * @author Dan Lyu
 */
public class TradeConfirmWidget extends TradeDetailWidget<TradeConfirmWidget.ConfirmationPair> {

    private final CheckBox doIConfirm;
    private final CheckBox doIConfirmTransaction;
    private final CheckBox doIConfirmTransactionBack;

    /**
     * The type Confirmation pair.
     *
     * @author Dan Lyu
     */
    public static class ConfirmationPair {
        /**
         * The Trade confirm.
         */
        public boolean tradeConfirm, /**
         * The Transaction confirm.
         */
        transactionConfirm, /**
         * The Transaction back confirm.
         */
        transactionBackConfirm;

        /**
         * Constructs a new Confirmation pair.
         *
         * @param tradeConfirm           the trade confirm
         * @param transactionConfirm     the transaction confirm
         * @param transactionBackConfirm the transaction back confirm
         */
        public ConfirmationPair(boolean tradeConfirm, boolean transactionConfirm, boolean transactionBackConfirm) {
            this.tradeConfirm = tradeConfirm;
            this.transactionConfirm = transactionConfirm;
            this.transactionBackConfirm = transactionBackConfirm;
        }
    }


    /**
     * Constructs a new Trade confirm widget.
     *
     * @param controllerResources the controller resources
     * @param tradeOrder          the trade order
     * @param previousValue       the previous value
     */
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
