package phase2.trade.view.widget;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.AccountState;
import phase2.trade.user.User;
import phase2.trade.user.command.ChangeAccountState;
import phase2.trade.view.window.AddressAlertController;

import java.net.URL;
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
}
