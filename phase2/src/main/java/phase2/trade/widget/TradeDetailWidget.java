package phase2.trade.widget;

import phase2.trade.controller.ControllerResources;
import phase2.trade.trade.TradeOrder;

public abstract class TradeDetailWidget<T> extends WidgetControllerBase {

    protected final TradeOrder tradeOrder;

    public TradeDetailWidget(ControllerResources controllerResources, TradeOrder tradeOrder) {
        super(controllerResources);
        this.tradeOrder = tradeOrder;
    }

    public abstract T getValue();
}
