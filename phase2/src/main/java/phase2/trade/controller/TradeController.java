package phase2.trade.controller;

import javafx.fxml.Initializable;
import phase2.trade.callback.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.TradeGateway;
import phase2.trade.item.Item;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeManager;
import phase2.trade.address.Address;
import phase2.trade.user.User;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;


public class TradeController extends AbstractController implements Initializable {

    private final TradeGateway tradeGateway;

    private final TradeManager tm;

    Trade newTrade;

    public TradeController(GatewayBundle gatewayBundle) {
        super(gatewayBundle);
        this.tradeGateway = gatewayBundle.getEntityBundle().getTradeGateway();
        this.tm = new TradeManager(gatewayBundle.getConfigBundle().getTradeConfig());
    }

    public Trade addTrade(Callback<Trade> callback, List<User> users, List<List<Item>> items, String year, String month,
                         String day, String hour, String minute, String country, String city, String street,
                         String streetNum, boolean isPermanent) {
        tradeGateway.submitTransaction(() -> {
            Trade newTrade = tm.createTrade(users, items, year, month, day, hour, minute, country, city, street,
                    streetNum, isPermanent);
            tradeGateway.add(newTrade);
            this.newTrade = newTrade;
            callback.call(newTrade);
        });
        return newTrade;
    }

    public void editMeetingDateAndTime(Callback<Trade> callback, Trade currTrade, User currUser, LocalDateTime dateTime){
        tradeGateway.submitTransaction(() -> {
            Trade trade = tm.editDateAndTime(currTrade, currUser, dateTime);
            tradeGateway.update(trade);
            callback.call(trade);
        });
    }

    public void editMeetingLocation(Callback<Trade> callback, Trade currTrade, User currUser, String
            country, String city, String street, String streetNumber) {
        tradeGateway.submitTransaction(() -> {
            Address location = new Address(country, city, street, streetNumber);
            Trade trade = tm.editLocation(currTrade, currUser, location);
            tradeGateway.update(trade);
            callback.call(trade);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Add getRecentTrades, getFrequentTraders, etc.
}
