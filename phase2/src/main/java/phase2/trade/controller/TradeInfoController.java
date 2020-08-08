package phase2.trade.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import phase2.trade.trade.Trade;

public class TradeInfoController {
    @FXML
    TextField year;
    @FXML
    TextField month;
    @FXML
    TextField day;
    @FXML
    TextField hour;
    @FXML
    TextField minute;
    @FXML
    TextField country;
    @FXML
    TextField city;
    @FXML
    TextField street;
    @FXML
    TextField streetNum;
    @FXML
    JFXComboBox<Boolean> isPermanent;

    public TradeInfoController(){
        HBox dateTime = new HBox();
        dateTime.setSpacing(10);
    }
}
