package phase2.trade.trade;

import phase2.trade.config.TradeConfig;
import phase2.trade.item.Item;
import phase2.trade.address.Address;
import phase2.trade.user.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
public class TradeManager {
    private TradeConfig tradeConfig;
    private TradeCreator tc;
    private TradeEditor te;
    private TradeConfirmer tcc;

    public TradeManager(TradeConfig tradeConfig){
        this.tradeConfig = tradeConfig;
        tc = new TradeCreator();
        te = new TradeEditor(tradeConfig.getEditLimit());
    }

    public Trade createTrade(List<User> users, List<List<Item>> items, String year, String month, String day, String
            hour, String minute, String country, String city, String street, String streetNum, boolean isPermanent){
        return tc.createTrade(users, items, year, month, day, hour, minute, country, city, street, streetNum,
                isPermanent);
    }

    public Trade editDateAndTime(Trade currTrade, User editingUser, LocalDateTime dateAndTime) {
        TradeEditor te = new TradeEditor(tradeConfig.getEditLimit());
        return te.editDateAndTime(currTrade, editingUser, dateAndTime);
    }

    public Trade editLocation(Trade currTrade, User editingUser, Address location) {
        TradeEditor te = new TradeEditor(tradeConfig.getEditLimit());
        return te.editLocation(currTrade, editingUser, location);
    }

    public Trade confirmTrade(Trade currTrade, User editingUser) { return tcc.confirmTrade(currTrade, editingUser);}
}

