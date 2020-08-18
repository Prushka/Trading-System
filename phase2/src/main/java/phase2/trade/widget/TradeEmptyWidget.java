package phase2.trade.widget;

import javafx.scene.control.Label;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;

import java.net.URL;
import java.util.ResourceBundle;

public class TradeEmptyWidget extends TradeDetailWidget<Void> {


    public TradeEmptyWidget(ControllerResources controllerResources, User leftSelected, User rightSelected) {
        super(controllerResources, leftSelected, rightSelected);
    }

    @Override
    public Void getValue() {
        return null;
    }

    public void setText(String text) {
        clear();
        addTitle(new Label(text));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-a");
        addTitle(new Label("Not Your Order"));
        refresh();
    }
}
