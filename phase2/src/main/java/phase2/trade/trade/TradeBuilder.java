package phase2.trade.trade;

import phase2.trade.item.Item;
import phase2.trade.user.Address;
import phase2.trade.user.PersonalUser;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
class TradeBuilder {

    Boolean isPermanent;
    List<PersonalUser> users;
    List<List<Item>> items;
    LocalDateTime dateAndTime;
    Address location;
    Long prevMeeting;

    Trade newTrade;

    TradeBuilder() {}

    void buildTraders(List<PersonalUser> users) {
        this.users = users;
    }

    void buildItems(List<List<Item>> items){
        this.items = items;
    }

    void buildDateAndTime(LocalDateTime dateAndTime){
        this.dateAndTime = dateAndTime;
    }

    void buildLocation(Address location){
        this.location = location;
    }

    void buildType(boolean isPermanent){
        this.isPermanent = isPermanent;
    }

    void buildPastMeeting(Long prevMeeting){
        this.prevMeeting = prevMeeting;
    }

   Trade buildTrade() {
        if (isPermanent) {
            newTrade = new PermanentTrade(users, items, dateAndTime, location, prevMeeting, null);
            newTrade.setStrategy(new PermanentTradingStrategy(newTrade));
        } else {
            newTrade = new TemporaryTrade(users, items, dateAndTime, location, prevMeeting, null);
            newTrade.setStrategy(new TemporaryTradingStrategy(newTrade));
        }
        return newTrade;
    }
}
