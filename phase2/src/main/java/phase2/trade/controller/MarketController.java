package phase2.trade.controller;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
// import phase2.trade.gateway.database.DatabaseResourceBundleImpl;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeState;
import phase2.trade.user.User;

import java.net.URL;
import java.util.ResourceBundle;

public class MarketController  extends AbstractController implements Initializable {
    User currUser;
    TradeInfoController tic;
    @FXML
    TableView<Trade> trades;
    @FXML
    ObservableList<Trade> tradeList = FXCollections.observableArrayList();
    @FXML
    Button submitBorrowTrade;
    @FXML
    Button submitLendTrade;
    @FXML
    Button submitThreeWayTrade;
    TableColumn<Trade, TradeState> statusColumn;
    // TradeCommand tc;

    private StringProperty submissionResultProperty;

    public Label submissionResult;

    public MarketController(ControllerResources controllerResources){
        super(controllerResources);
        // tc = new CreateTrade(gatewayBundle, (RegularUser) currUser);
        // tic = new TradeInfoController(gatewayBundle);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
        tradeList.add(tc.addTrade(result -> {
                    if (result != null) {
                        System.out.println("success");
                    }
                }, new ArrayList<>(), new ArrayList<>(), "2020", "9", "8", "1", "1",
                "country", "city", "street", "num", true));

         */
    }

    public void borrowTradeClicked(){
//        Trade newTrade = tc.execute(,tic.getYear(), tic.getCity(), tic.getDay(), tic.getHour(),
//                tic.getMinute(), tic.getCountry(), tic.getCity(), tic.getStreet(), tic.getStreetNum(), "true");
//        trades.getItems().add(newTrade);
    }

    public void lendTradeClicked(){
//        Trade newTrade = tc.execute(, tic.getYear(), tic.getCity(), tic.getDay(), tic.getHour(),
//                tic.getMinute(), tic.getCountry(), tic.getCity(), tic.getStreet(), tic.getStreetNum(), "true");
//        trades.getItems().add(newTrade);
    }

    public void threeWayTradeClicked(){
//        Trade newTrade = tc.execute(, tic.getYear(), tic.getCity(), tic.getDay(), tic.getHour(),
//                tic.getMinute(), tic.getCountry(), tic.getCity(), tic.getStreet(), tic.getStreetNum(), "true");
//        trades.getItems().add(newTrade);
    }
}
