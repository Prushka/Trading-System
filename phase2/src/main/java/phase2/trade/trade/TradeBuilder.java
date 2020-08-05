package phase2.trade.trade;

import net.bytebuddy.asm.Advice;
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
class TradeBuilder {

    Trade newTrade;

    // For UserOrderBundle(s)
    User initiator;
    User target;
    List<Item> items;

    // For Order(s)
    LocalDateTime dateAndTime;
    OrderState type;
    Address location;

    // For a Trade
    Boolean isPermanent;
    List<Order> orders;

    TradeBuilder() {}

    void buildUserOrderBundle(User user, List<Item> items){ this.initiator = user; }

    void build(){}

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
