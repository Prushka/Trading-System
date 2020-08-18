package phase2.trade.widget;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.CheckBox;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;

import java.net.URL;
import java.util.ResourceBundle;

public class TradeConfirmWidget extends TradeDetailWidget {

    private final CheckBox doIConfirm;


    public TradeConfirmWidget(ControllerResources controllerResources, User leftSelected, User rightSelected) {
        super(controllerResources, leftSelected, rightSelected);
        doIConfirm = new JFXCheckBox("Confirm");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-a");
        addTitle(doIConfirm);
        refresh();
    }
}
