package phase2.trade.widget;

import phase2.trade.controller.ControllerResources;

import java.net.URL;
import java.util.ResourceBundle;

public class TradeEmptyWidget extends LittleTextWidgetController {
    public TradeEmptyWidget(ControllerResources controllerResources, String title, String... contents) {
        super(controllerResources, title, contents);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setGradient("gradient-a");
        refresh();
    }
}
