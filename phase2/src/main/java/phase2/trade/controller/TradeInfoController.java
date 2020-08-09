package phase2.trade.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import phase2.trade.trade.Trade;

import java.net.URL;
import java.util.ResourceBundle;

public class TradeInfoController implements Initializable {
    @FXML
    HBox meeting = new HBox();
    @FXML
    private TextField year;
    @FXML
    private TextField month;
    @FXML
    private TextField day;
    @FXML
    private TextField hour;
    @FXML
    private TextField minute;
    @FXML
    private TextField country;
    @FXML
    private TextField city;
    @FXML
    private TextField street;
    @FXML
    private TextField streetNum;
    @FXML
    private JFXComboBox<Boolean> isPermanent;

    public TradeInfoController(){
        HBox dateTime = new HBox();
        dateTime.setSpacing(10);
    }

    public String getYear() {
        String text = year.getText();
        year.clear();
        return text;
    }

    public String getMonth() {
        String text = month.getText();
        month.clear();
        return text;
    }

    public String getDay() {
        String text = day.getText();
        day.clear();
        return text;
    }

    public String getHour() {
        String text = hour.getText();
        hour.clear();
        return text;
    }

    public String getMinute() {
        String text = minute.getText();
        minute.clear();
        return text;
    }

    public String getCountry() {
        String text = country.getText();
        country.clear();
        return text;
    }

    public String getCity() {
        String text = city.getText();
        city.clear();
        return text;
    }

    public String getStreet() {
        String text = street.getText();
        street.clear();
        return text;
    }

    public String getStreetNum() {
        String text = streetNum.getText();
        streetNum.clear();
        return text;
    }

    public boolean getIsPermanent() { return isPermanent.getSelectionModel().getSelectedItem(); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
