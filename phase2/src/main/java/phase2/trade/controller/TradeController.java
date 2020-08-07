package phase2.trade.controller;

import javafx.fxml.Initializable;
import phase2.trade.config.property.TradeProperties;
import phase2.trade.database.Callback;
import phase2.trade.database.DatabaseResourceBundle;
import phase2.trade.database.TradeDAO;
import phase2.trade.item.Item;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeManager;
import phase2.trade.user.Address;
import phase2.trade.user.User;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;


public class TradeController extends AbstractController implements Initializable {

    private final TradeDAO tradeDAO;

    private final TradeManager tm;

    public TradeController(DatabaseResourceBundle databaseResourceBundle, TradeDAO tradeDAO, TradeProperties
            tradeProperties) {
        super(databaseResourceBundle);
        this.tradeDAO = tradeDAO;
        this.tm = new TradeManager(tradeProperties);
    }

    public void addTrade(Callback<Trade> callback, List<User> users, List<List<Item>> items, String year, String month,
                         String day, String hour, String minute, String country, String city, String street,
                         String streetNum, boolean isPermanent) {
        tradeDAO.submitSessionWithTransaction(() -> {
            Trade newTrade = tm.createTrade(users, items, year, month, day, hour, minute, country, city, street,
                    streetNum, isPermanent);
            tradeDAO.add(newTrade);
            callback.call(newTrade);
        });
    }

    public void editMeetingDateAndTime(Callback<Trade> callback, Trade currTrade, User currUser, LocalDateTime dateTime){
        tradeDAO.submitSessionWithTransaction(() -> {
            Trade trade = tm.editDateAndTime(currTrade, currUser, dateTime);
            tradeDAO.update(trade);
            callback.call(trade);
        });
    }

    public void editMeetingLocation(Callback<Trade> callback, Trade currTrade, User currUser, String
            country, String city, String street, String streetNumber) {
        tradeDAO.submitSessionWithTransaction(() -> {
            Address location = new Address(country, city, street, streetNumber);
            Trade trade = tm.editLocation(currTrade, currUser, location);
            tradeDAO.update(trade);
            callback.call(trade);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Add getRecentTrades, getFrequentTraders, etc.
}
