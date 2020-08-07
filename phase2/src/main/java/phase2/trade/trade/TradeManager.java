package phase2.trade.trade;

import phase2.trade.config.property.TradeProperties;
import phase2.trade.database.Callback;
import phase2.trade.database.TradeDAO;
import phase2.trade.item.Item;
import phase2.trade.user.Address;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
public class TradeManager {
    private TradeProperties tradeProperties;
    private Integer editLimit;
    private Integer timeLimit;
    private TradeCreator tc;
    private TradeEditor te;

    public TradeManager(TradeProperties tradeProperties){
        this.tradeProperties = tradeProperties;
        editLimit = tradeProperties.getInt("editLimit");
        timeLimit = tradeProperties.getInt("timeLimit");
        tc = new TradeCreator();
        te = new TradeEditor(editLimit);
    }

    public Trade createTrade(List<User> users, List<List<Item>> items, String year, String month, String day, String
            hour, String minute, String country, String city, String street, String streetNum, boolean isPermanent){
        return tc.createTrade(users, items, year, month, day, hour, minute, country, city, street, streetNum,
                isPermanent);
    }

    public Trade editDateAndTime(Trade currTrade, User editingUser, LocalDateTime dateAndTime) {
        TradeEditor te = new TradeEditor(editLimit);
        return te.editDateAndTime(currTrade, editingUser, dateAndTime);
    }

    public Trade editLocation(Trade currTrade, User editingUser, Address location) {
        TradeEditor te = new TradeEditor(editLimit);
        return te.editLocation(currTrade, editingUser, location);
    }

    public Trade confirmTrade(Trade currTrade, User editingUser) { return currTrade.getStrategy().confirmTrade(editingUser);}
}

