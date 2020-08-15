package phase2.trade.trade;

import phase2.trade.item.Item;
import phase2.trade.address.Address;
import phase2.trade.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    LocalDateTime dateAndTime;
    Address location;
    TradeOrder order;

    // For a Trade
    Boolean isPermanent;

    TradeBuilder() {}

    void buildUserOrderBundle(User user, Set<Item> items){
        uobb.buildUser(user);
        uobb.buildDesiredItems(items);
        UserOrderBundle newBundle = uobb.buildUserOrderBundle();
        traders.add(newBundle);
    }

    void buildDateTime(String year, String month, String day, String hour, String minute){
        this.dateAndTime = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month),
                Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
        OnlineOrder newOrder = new OnlineOrder();
        newOrder.setDateAndTime(dateAndTime);
        order = newOrder;
    }

    void buildLocation(String country, String city, String street, String streetNum){
        this.location = new Address(country, city, street, streetNum);
        MeetUpOrder newOrder = new MeetUpOrder();
        newOrder.setDateAndTime(dateAndTime);
        newOrder.setLocation(location);
        order = newOrder;
    }

    void buildIsPermanent(boolean isPermanent){ this.isPermanent = isPermanent; }

    Trade buildTrade() {
        newTrade = new Trade();
        newTrade.setOrder(order);
        newTrade.setIsPermanent(isPermanent);
        newTrade.setTradeState(TradeState.IN_PROGRESS);
        return newTrade;
    }
}
