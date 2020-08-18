package phase2.trade.trade.use;

import phase2.trade.address.Address;
import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.config.TradeConfig;
import phase2.trade.trade.OrderState;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.user.User;

import java.time.LocalDateTime;

public class EditTrade {

    private final Trade trade;

    private final User operator;

    private final TradeConfig tradeConfig;

    public EditTrade(Trade trade, User operator, TradeConfig tradeConfig) {
        this.trade = trade;
        this.operator = operator;
        this.tradeConfig = tradeConfig;
    }


    public void editAddress(TradeOrder tradeOrder, Address newAddress, StatusCallback statusCallback) {
        UserOrderBundle operatorBundle = tradeOrder.findBundleByUser(operator);
        if (operatorBundle == null || newAddress == null || newAddress.equals(tradeOrder.getAddressTrade())) return;
        tradeOrder.findBundleByUser(operator).edit();
        if (ifExceedLimit(operatorBundle.getEdits(), operatorBundle.hasConfirmed())) {
            tradeOrder.setOrderState(OrderState.CANCELLED);
            statusCallback.call(new StatusFailed("your.order.has.been.cancelled"));
        }
        tradeOrder.setAddressTrade(newAddress);
    }

    public void editTime(TradeOrder tradeOrder, LocalDateTime newDate, StatusCallback statusCallback) {
        UserOrderBundle operatorBundle = tradeOrder.findBundleByUser(operator);
        if (operatorBundle == null || newDate == null || newDate.equals(tradeOrder.getDateAndTime())) return;
        tradeOrder.findBundleByUser(operator).edit();
        if (ifExceedLimit(operatorBundle.getEdits(), operatorBundle.hasConfirmed())) {
            tradeOrder.setOrderState(OrderState.CANCELLED);
            statusCallback.call(new StatusFailed("your.order.has.been.cancelled-" + operatorBundle.getEdits() + "-" + tradeConfig.getEditLimit()));
        }
        tradeOrder.findBundleByUser(operator).edit();
        tradeOrder.setDateAndTime(newDate);
    }

    public void editConfirm(TradeOrder tradeOrder, Boolean hasConfirmed, StatusCallback statusCallback) {
        tradeOrder.findBundleByUser(operator).setConfirm(hasConfirmed);
        if (tradeOrder.getRightBundle().hasConfirmed() && tradeOrder.getLeftBundle().hasConfirmed()) {
            tradeOrder.setOrderState(OrderState.PENDING_TRADE);
        }
    }

    private boolean ifExceedLimit(int edits, boolean hasConfirmed) {
        return edits >= tradeConfig.getEditLimit() && !hasConfirmed;
    }
}
