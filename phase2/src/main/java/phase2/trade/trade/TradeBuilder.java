package phase2.trade.trade;

import phase2.trade.item.Item;
import phase2.trade.user.Address;
import phase2.trade.user.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
class TradeBuilder {

    Trade newTrade;

    // For UserOrderBundle(s)
    List<UserOrderBundle> traders;
    UserOrderBundleBuilder uobb;

    // For Order(s)
    Order order;

    // For a Trade
    Boolean isPermanent;
    TradeState type;

    TradeBuilder() {}

    void buildUserOrderBundle(User user, List<Item> items){
        uobb.buildUser(user);
        uobb.buildDesiredItems(items);
        UserOrderBundle newBundle = uobb.buildUserOrderBundle();
        traders.add(newBundle);
    }

    void buildOrder(LocalDateTime dateAndTime, Address location){
        MeetUpOrder newOrder = new MeetUpOrder();
        newOrder.setDateAndTime(dateAndTime);
        newOrder.setLocation(location);
        order = newOrder;
    }

    void buildIsPermanent(boolean isPermanent){ this.isPermanent = isPermanent; }

    Trade buildTrade() {
        if (isPermanent) {
            newTrade = new PermanentTrade();
            newTrade.setStrategy(new PermanentTradingStrategy(newTrade));
        } else {
            newTrade = new TemporaryTrade();
            newTrade.setStrategy(new TemporaryTradingStrategy(newTrade));
        }
        return newTrade;
    }
}
