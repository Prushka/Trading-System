package main.java.phase2.trade.exclude.trade;

import java.time.LocalDateTime;
import java.util.List;

class PermanentTrade extends Trade{

    Tradable strategy;

    PermanentTrade(List<Integer> users, List<List<Integer>> items, LocalDateTime dateAndTime, String
            location, Integer prevMeeting, Tradable strategy){
        super(users, items, dateAndTime, location, prevMeeting);
        this.strategy = strategy;
    }

    Tradable getStrategy(){ return strategy; }
}
