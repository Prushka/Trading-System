package phase2.trade.widget;

import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;

public abstract class TradeDetailWidget<T> extends WidgetControllerBase {

    protected final User leftSelected, rightSelected;

    public TradeDetailWidget(ControllerResources controllerResources, User leftSelected, User rightSelected) {
        super(controllerResources);
        this.leftSelected = leftSelected;
        this.rightSelected = rightSelected;
    }

    public abstract T getValue();
}