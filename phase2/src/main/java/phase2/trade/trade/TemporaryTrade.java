package phase2.trade.trade;

import phase2.trade.item.Item;
import phase2.trade.user.Address;
import phase2.trade.user.PersonalUser;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a permanent trade between users at a specific date and place
 * @author Grace Leung
 */
@Entity
class TemporaryTrade extends Trade{

    Tradable strategy;

    TemporaryTrade(){
        super();
    }

    Tradable getStrategy(){ return strategy; }
    void setStrategy(Tradable newStrategy){ strategy = newStrategy; }
}
