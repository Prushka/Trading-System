package phase2.trade.trade;

import javax.persistence.Entity;

/**
 * Represents a permanent trade between users at a specific date and place
 * @author Grace Leung
 */
@Entity
class PermanentTrade extends Trade{

    Tradable strategy;

    PermanentTrade(){
        super();
    }

    Tradable getStrategy(){ return strategy; }
    void setStrategy(Tradable newStrategy){ strategy = newStrategy; }
}
