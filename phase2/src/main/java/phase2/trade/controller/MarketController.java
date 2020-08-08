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
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeState;
import phase2.trade.user.User;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MarketController  extends AbstractController implements Initializable {
    User currUser;
    TradeController tc;
    TableView<Trade> trades;
    Button submitBorrowTrade = new Button();
    Button submitLendTrade = new Button();
    Button submitTwoWayTrade = new Button();
    Button submitThreeWayTrade = new Button();

    public MarketController(GatewayBundle gatewayBundle, User currUser){
        super(gatewayBundle);
        this.currUser = currUser;
        // tc = new TradeController(gatewayBundle, new TradeProperties(new SaveHook())); // need to change trade properties thing
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
