package phase2.trade.trade;

import phase2.trade.item.Item;
import phase2.trade.user.Address;
import phase2.trade.user.PersonalUser;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a permanent trade between users at a specific date and place
 * @author Grace Leung
 */
class PermanentTrade extends Trade{

    Tradable strategy;

    /**
     * @param users All the userID's associated with this trade
     * @param items All the items associated with this trade. Each list corresponds to the desired
     *              items of the userID in users with the same index
     * @param dateAndTime When this trade takes place
     * @param location Where this trade takes place
     * @param prevMeeting The trade ID of the previous meeting
     * @param strategy The strategy that this trade uses
     */
    PermanentTrade(List<PersonalUser> users, List<List<Item>> items, LocalDateTime dateAndTime, Address
            location, Long prevMeeting, Tradable strategy){
        super(users, items, dateAndTime, location, prevMeeting);
        this.strategy = strategy;
    }

    Tradable getStrategy(){ return strategy; }
    void setStrategy(Tradable newStrategy){ strategy = newStrategy; }
}
