package phase2.trade.widget;

import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import phase2.trade.controller.ControllerResources;
import phase2.trade.trade.TradeOrder;

import java.net.URL;
import java.util.ResourceBundle;

public class TradeConfirmWidget extends TradeDetailWidget<Boolean> {

    private final CheckBox doIConfirm;


    public TradeConfirmWidget(ControllerResources controllerResources, TradeOrder tradeOrder, Boolean previousValue) {
        super(controllerResources, tradeOrder);
        doIConfirm = new JFXCheckBox("Confirm");
        doIConfirm.setSelected(previousValue);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-a");
        addTitle(doIConfirm);
        addContent(new Label("As user: " + getAccountManager().getLoggedInUser().getName()));
        refresh();
    }

    public Boolean getValue() {
        return doIConfirm.isSelected();
    }
}
