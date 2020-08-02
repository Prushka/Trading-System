package main.java.phase2.trade.exclude.trade;

import java.time.LocalDateTime;
import java.util.*;

public class TradeManagerFacade {
    private TradeBuilder tb = new TradeBuilder();
    private TradeEditor te = new TradeEditor();
    private Integer editLimit = 3;
    private Integer timeLimit = 1;

    public TradeManagerFacade() {}

    /**
     * Creates a trade meeting to trade items temporarily
     * @param users All the userID's associated with this trade
     * @param items All the items associated with this trade. Each list corresponds to the desired
     *              items of the userID in users with the same index
     * @param dateAndTime When this trade takes place
     * @param location Where this trade takes place
     * @param prevMeeting The tradeID of the previous meeting
     */
    public Trade createTrade(int num, List<Integer> users, List<List<Integer>> items, LocalDateTime
            dateAndTime, String location, Integer prevMeeting) {
        TradeBuilder tb = new TradeBuilder();
        tb.buildAmountOfTraders(num);
        tb.buildTraders(users);
        tb.buildItems(items);
        tb.buildDateAndTime(dateAndTime);
        tb.buildLocation(location);
        tb.buildType(true);
        tb.buildPastMeeting(prevMeeting);
        Trade newTrade = tb.buildTrade();
        // TODO: add trade to database and add trade to each user's all trades list
        return newTrade;
    }

    public void editDateAndTime(int tradeID, int editingUser, LocalDateTime dateAndTime) { te.editDateAndTime(tradeID,
            editingUser, dateAndTime);}

    public void editLocation(int tradeID, int editingUser, String location) { te.editLocation(tradeID,
            editingUser, location);}

    // public void confirmTrade(int tradeID, int editingUser) { }
    public void confirmTrade(int tradeID, int editingUser, Trade currTrade) { currTrade.getStrategy().confirmTrade(tradeID, editingUser);}
}
