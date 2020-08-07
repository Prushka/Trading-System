package phase2.trade.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeState;
import phase2.trade.user.AccountManager;
import phase2.trade.user.User;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MarketController implements Initializable {
    GridPane grid = new GridPane();
    TableView<Trade> trades;
    TextField traders;
    TextField items;
    TextField year;
    TextField month;
    TextField day;
    TextField hour;
    TextField minute;
    TextField country;
    TextField city;
    TextField street;
    TextField streetNum;
    JFXComboBox<Boolean> isPermanent;
    Button submit;

    User currUser;
    public MarketController(User currUser){
        this.currUser = currUser;
        TableColumn<Trade, TradeState> statusColumn = new TableColumn<>("Trade Status: ");
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("tradeState"));

        HBox dateTime = new HBox();
        dateTime.setSpacing(10);

        trades = new TableView<>();
        trades.setItems(getTrades());
        trades.getColumns().setAll(statusColumn);

        grid.setVgap(10); // Adds spacing veritcally
        grid.setHgap(10); // Adds spacing horizontally
        GridPane.setConstraints(trades, 0, 0); // adds items to the gridpane at the column and row
        GridPane.setConstraints(dateTime, 1, 0); // adds items to the gridpane at the column and row
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public ObservableList<Trade> getTrades(){
        ObservableList<Trade> tradeList = FXCollections.observableArrayList();
        return tradeList;
    }
}
