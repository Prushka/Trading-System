package phase2.trade.widget;

import phase2.trade.controller.ControllerResources;
import phase2.trade.trade.TradeOrder;

/**
 * The Trade detail widget.
 *
 * @param <T> the type parameter
 * @author Dan Lyu
 */
public abstract class TradeDetailWidget<T> extends WidgetControllerBase {

    /**
     * The Trade order.
     */
    protected final TradeOrder tradeOrder;

    /**
     * Constructs a new Trade detail widget.
     *
     * @param controllerResources the controller resources
     * @param tradeOrder          the trade order
     */
    public TradeDetailWidget(ControllerResources controllerResources, TradeOrder tradeOrder) {
        super(controllerResources);
        this.tradeOrder = tradeOrder;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public abstract T getValue();
}
