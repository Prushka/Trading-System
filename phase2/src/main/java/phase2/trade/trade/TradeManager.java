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
        tcc = new TradeConfirmer(tradeConfig.getTimeLimit());
    }

//    public Trade createTrade(){
//        return tc.buildTrade();
//    }
//
//    public Trade editDateAndTime(Trade currTrade, Long orderID, User editingUser, LocalDateTime dateAndTime) {
//        return te.editDateAndTime(currTrade, orderID, editingUser, dateAndTime);
//    }
//
//    public Trade editLocation(Trade currTrade, Long orderID, User editingUser, Address location) {
//        return te.editLocation(currTrade, orderID, editingUser, location);
//    }
//
//    public void confirmTrade(int editingUser, Trade currTrade) {
//        currTrade.getStrategy().confirmTrade(editingUser);
//    }
}

