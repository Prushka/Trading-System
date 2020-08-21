package phase2.trade.widget;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import phase2.trade.controller.ControllerResources;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Trade option widget.
 *
 * @author Dan Lyu
 */
public class TradeOptionWidget extends WidgetControllerBase {

    private final Button finishTrade = getNodeFactory().getDefaultFlatButton("Finish Trade", "widget-button");

    private EventHandler<ActionEvent> eventHandler;

    /**
     * Constructs a new Trade option widget.
     *
     * @param controllerResources the controller resources
     */
    public TradeOptionWidget(ControllerResources controllerResources) {
        super(controllerResources);
    }

    /**
     * Sets event handler.
     *
     * @param eventHandler the event handler
     */
    public void setEventHandler(EventHandler<ActionEvent> eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-j");
        addContent(finishTrade);
        if (eventHandler != null)
            finishTrade.setOnAction(eventHandler);
    }

}
