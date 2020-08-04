package phase2.trade.trade;

import phase2.trade.config.property.TradeProperties;
import phase2.trade.database.Callback;
import phase2.trade.database.TradeDAO;
import phase2.trade.item.Item;
import phase2.trade.user.Address;
import phase2.trade.user.PersonalUser;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
public class TradeManager {
    private final TradeDAO tradeDAO;
    private TradeProperties tradeProperties;
    private Integer editLimit;
    private Integer timeLimit;

    public TradeManager(TradeDAO tradeDAO, TradeProperties tradeProperties){
        this.tradeDAO = tradeDAO;
        editLimit = tradeProperties.getInt("editLimit");
        timeLimit = tradeProperties.getInt("timeLimit");
    }

    /**
     * Creates a trade meeting to trade items temporarily
     * @param users All the userID's associated with this trade
     * @param items All the items associated with this trade. Each list corresponds to the desired
     *              items of the userID in users with the same index
     * @param dateAndTime When this trade takes place
     * @param location Where this trade takes place
     * @param prevMeeting The tradeID of the previous meeting
     */
    public void addTrade(Callback<Trade> callback, List<PersonalUser> users, List<List<Item>> items, LocalDateTime
            dateAndTime, Address location, Long prevMeeting, boolean isPermanent) {
        tradeDAO.submitSessionWithTransaction(() -> {
            TradeBuilder tb = new TradeBuilder();
            tb.buildTraders(users);
            tb.buildItems(items);
            tb.buildDateAndTime(dateAndTime);
            tb.buildLocation(location);
            tb.buildType(isPermanent);
            tb.buildPastMeeting(prevMeeting);
            Trade newTrade = tb.buildTrade();
            tradeDAO.add(newTrade);
            callback.call(newTrade);
        });
    }

    public void editDateAndTime(int tradeID, int editingUser, LocalDateTime dateAndTime) {
        TradeEditor te = new TradeEditor(editLimit);
        Trade currTrade = tradeDAO.findById(tradeID);
        te.editDateAndTime(currTrade, editingUser, dateAndTime);}

    public void editLocation(int tradeID, int editingUser, Address location) {
        Trade currTrade = tradeDAO.findById(tradeID);
        TradeEditor te = new TradeEditor(editLimit);
        te.editLocation(currTrade, editingUser, location);}

    public void confirmTrade(int editingUser, Trade currTrade) { currTrade.getStrategy().confirmTrade(editingUser);}
}

