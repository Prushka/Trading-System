package phase2.trade.trade;

/**
 * The enum Order state.
 *
 * @author Dan Lyu
 * @author Grace Leung
 */
public enum OrderState {
    /**
     * Pending confirmation order state.
     */
    PENDING_CONFIRMATION(true),
    /**
     * Pending trade order state.
     */
    PENDING_TRADE(true),
    /**
     * Pending trade back order state.
     */
    PENDING_TRADE_BACK(true),
    /**
     * Closed order state.
     */
    CLOSED(false),
    /**
     * Cancelled order state.
     */
    CANCELLED(false);

    /**
     * The Editable.
     */
    public boolean editable;

    OrderState(boolean editable) {
        this.editable = editable;
    }
}
