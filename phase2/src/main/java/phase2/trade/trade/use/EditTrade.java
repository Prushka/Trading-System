package phase2.trade.trade.use;

import phase2.trade.address.Address;
import phase2.trade.callback.status.ResultStatusWrapper;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.config.TradeConfig;
import phase2.trade.trade.OrderState;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.user.User;
import phase2.trade.widget.TradeConfirmWidget;

import java.time.LocalDateTime;

/**
 * The Edit trade.
 *
 * @author Dan Lyu
 */
public class EditTrade {

    private final Trade trade;

    private final User operator;

    private final TradeConfig tradeConfig;

    /**
     * Constructs a new Edit trade.
     *
     * @param trade       the trade
     * @param operator    the operator
     * @param tradeConfig the trade config
     */
    public EditTrade(Trade trade, User operator, TradeConfig tradeConfig) {
        this.trade = trade;
        this.operator = operator;
        this.tradeConfig = tradeConfig;
    }


    /**
     * Edit address result status wrapper.
     *
     * @param tradeOrder the trade order
     * @param newAddress the new address
     * @return the result status wrapper
     */
    public ResultStatusWrapper<Trade> editAddress(TradeOrder tradeOrder, Address newAddress) {
        UserOrderBundle operatorBundle = tradeOrder.findBundleByUser(operator);
        if (operatorBundle == null || newAddress == null || newAddress.equals(tradeOrder.getAddressTrade()))
            return new ResultStatusWrapper<>(trade, new StatusSucceeded());
        if (ifExceedLimit(operatorBundle.getEdits(), operatorBundle.isTradeConfirmed())) {
            tradeOrder.setOrderState(OrderState.CANCELLED);
            return new ResultStatusWrapper<>(trade, new StatusFailed("your.order.has.been.cancelled"));
        }
        tradeOrder.findBundleByUser(operator).edit();
        tradeOrder.setAddressTrade(newAddress);
        return new ResultStatusWrapper<>(trade, new StatusSucceeded());
    }

    /**
     * Edit time result status wrapper.
     *
     * @param tradeOrder the trade order
     * @param newDate    the new date
     * @return the result status wrapper
     */
    public ResultStatusWrapper<Trade> editTime(TradeOrder tradeOrder, LocalDateTime newDate) {
        UserOrderBundle operatorBundle = tradeOrder.findBundleByUser(operator);
        if (operatorBundle == null || newDate == null || newDate.equals(tradeOrder.getDateAndTime()))
            return new ResultStatusWrapper<>(trade, new StatusSucceeded());
        if (ifExceedLimit(operatorBundle.getEdits(), operatorBundle.isTradeConfirmed())) {
            tradeOrder.setOrderState(OrderState.CANCELLED);
            return new ResultStatusWrapper<>(trade, new StatusFailed("your.order.has.been.cancelled-" + operatorBundle.getEdits() + "-" + tradeConfig.getEditLimit()));
        }
        tradeOrder.findBundleByUser(operator).edit();
        tradeOrder.setDateAndTime(newDate);
        return new ResultStatusWrapper<>(trade, new StatusSucceeded());
    }

    /**
     * Edit confirm.
     *
     * @param tradeOrder   the trade order
     * @param hasConfirmed the has confirmed
     */
    public void editConfirm(TradeOrder tradeOrder, TradeConfirmWidget.ConfirmationPair hasConfirmed) {
        tradeOrder.findBundleByUser(operator).setTradeConfirmed(hasConfirmed.tradeConfirm);
        tradeOrder.findBundleByUser(operator).setTransactionConfirmed(hasConfirmed.transactionConfirm);
        tradeOrder.findBundleByUser(operator).setTransactionBackConfirmed(hasConfirmed.transactionBackConfirm);
        tradeOrder.updateState();
    }

    private boolean ifExceedLimit(int edits, boolean hasConfirmed) {
        return edits > tradeConfig.getEditLimit() && !hasConfirmed;
    }
}
