package phase2.trade.widget;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class TradeTimeWidget extends TradeDetailWidget {

    private final DatePicker datePicker = new JFXDatePicker();
    private final JFXTimePicker timePicker = new JFXTimePicker();

    private final Label userLabel = new Label();

    public TradeTimeWidget(ControllerResources controllerResources, User leftSelected, User rightSelected) {
        super(controllerResources, leftSelected, rightSelected);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-l");
        userLabel.setText("Between " + leftSelected.getName() + " & " + rightSelected.getName());
        addContent(userLabel);
        addContent(datePicker, timePicker);
        getRoot().setPrefWidth(350);
        getRoot().setSpacing(15);
        getRoot().setPrefHeight(200);
        refresh();
    }

    public LocalDateTime getTime() {
        LocalDate localDate = datePicker.getValue();
        LocalTime localTime = timePicker.getValue();
        if (localDate == null || localTime == null) return null;
        return localDate.atTime(timePicker.getValue());
    }
}
