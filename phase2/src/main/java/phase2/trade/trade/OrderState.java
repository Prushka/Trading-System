package phase2.trade.trade;

public enum OrderState {
    PENDING_CONFIRMATION(true),
    PENDING_TRADE(true),
    PENDING_TRADE_BACK(true),
    CLOSED(false),
    CANCELLED(false);

    public boolean editable;

    OrderState(boolean editable) {
        this.editable = editable;
    }
}
