package phase2.trade.trade;

public enum OrderState {
    PENDING_CONFIRMATION,
    IN_PROGRESS,
    PENDING_TRADE,
    PENDING_TRADE_BACK,
    CLOSED,
    CANCELLED,
}
