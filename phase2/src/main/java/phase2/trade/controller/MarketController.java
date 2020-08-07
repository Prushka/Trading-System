package phase2.trade.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeState;
import phase2.trade.user.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MarketController implements Initializable {
    TableView<Trade> trades;
    JFXTextField traders;
    JFXTextField year;
    JFXTextField month;
    JFXTextField day;
    JFXTextField hour;
    JFXTextField minute;
    JFXTextField country;
    JFXTextField city;
    JFXTextField street;
    JFXTextField streetNum;
    JFXComboBox<Boolean> isPermanent;
    Button submit;

    public MarketController(){
        TableColumn<Trade, TradeState> statusColumn = new TableColumn<>("Trade Status: ");
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("tradeState"));

        trades = new TableView<>();
        trades.setItems(getTrades());
        trades.getColumns().setAll(statusColumn);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public ObservableList<Trade> getTrades(){
        ObservableList<Trade> tradeList = FXCollections.observableArrayList();
        return tradeList;
    }
}
