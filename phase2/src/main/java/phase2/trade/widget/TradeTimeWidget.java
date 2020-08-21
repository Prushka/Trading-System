package phase2.trade.widget;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import phase2.trade.controller.ControllerResources;
import phase2.trade.trade.TradeOrder;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * The Trade time widget.
 *
 * @author Dan Lyu
 */
public class TradeTimeWidget extends TradeDetailWidget<LocalDateTime> {

    private final DatePicker datePicker = new JFXDatePicker();
    private final JFXTimePicker timePicker = new JFXTimePicker();

    private final Label userLabel = new Label();

    private final LocalDateTime previousValue;

    /**
     * Constructs a new Trade time widget.
     *
     * @param controllerResources the controller resources
     * @param tradeOrder          the trade order
     */
    public TradeTimeWidget(ControllerResources controllerResources, TradeOrder tradeOrder) {
        this(controllerResources, tradeOrder, null);
    }

    /**
     * Constructs a new Trade time widget.
     *
     * @param controllerResources the controller resources
     * @param tradeOrder          the trade order
     * @param previousValue       the previous value
     */
    public TradeTimeWidget(ControllerResources controllerResources, TradeOrder tradeOrder,
                           LocalDateTime previousValue) {
        super(controllerResources, tradeOrder);
        this.previousValue = previousValue;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-l");
        userLabel.setText("Between " + tradeOrder.getLeftUser().getName() + " & " + tradeOrder.getRightUser().getName());
        addContent(userLabel);
        addContent(datePicker, timePicker);
        getRoot().setPrefWidth(350);
        getRoot().setSpacing(15);
        getRoot().setPrefHeight(200);
        if (previousValue != null) {
            datePicker.setValue(previousValue.toLocalDate());
            timePicker.setValue(previousValue.toLocalTime());
        }
        refresh();
    }

    public LocalDateTime getValue() {
        LocalDate localDate = datePicker.getValue();
        LocalTime localTime = timePicker.getValue();
        if (localDate == null || localTime == null) return null;
        return localDate.atTime(timePicker.getValue());
    }
}
