package phase2.trade.widget;

import phase2.trade.controller.ControllerResources;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Trade empty widget.
 *
 * @author Dan Lyu
 */
public class TradeEmptyWidget extends LittleTextWidgetController {
    /**
     * Constructs a new Trade empty widget.
     *
     * @param controllerResources the controller resources
     * @param title               the title
     * @param contents            the contents
     */
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
