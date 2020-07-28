package main.java.com.phase2.trade.trade;

import java.time.LocalDateTime;
import java.util.List;

class PermanentTrade extends Trade {

    /**
     * @param users All the userID's associated with this trade
     * @param items All the items associated with this trade. Each list corresponds to the desired
     *              items of the userID in users with the same index
     * @param dateAndTime When this trade takes place
     * @param location Where this trade takes place
     */
    PermanentTrade(List<Integer> users, List<List<Integer>> items, LocalDateTime dateAndTime,
                          String location){
        super(users, items, dateAndTime, location);
    }
}
