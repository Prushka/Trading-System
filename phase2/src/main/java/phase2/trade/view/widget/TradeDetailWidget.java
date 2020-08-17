package phase2.trade.view.widget;

import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class TradeDetailWidget extends WidgetControllerBase {

    protected final User leftSelected, rightSelected;

    public TradeDetailWidget(ControllerResources controllerResources, User leftSelected, User rightSelected) {
        super(controllerResources);
        this.leftSelected = leftSelected;
        this.rightSelected = rightSelected;
    }
}
