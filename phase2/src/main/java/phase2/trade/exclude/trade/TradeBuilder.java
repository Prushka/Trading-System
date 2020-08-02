package main.java.phase2.trade.exclude.trade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
class TradeBuilder {

    Boolean isPermanent;
    List<Integer> users;
    List<List<Integer>> items;
    LocalDateTime dateAndTime;
    String location;
    Integer prevMeeting;

    Trade newTrade;

    TradeBuilder() {}

    void buildTraders(List<Integer> users) {
        this.users = users;
    }

    void buildItems(List<List<Integer>> items){
        this.items = items;
    }

    void buildDateAndTime(LocalDateTime dateAndTime){
        this.dateAndTime = dateAndTime;
    }

    void buildLocation(String location){
        this.location = location;
    }

    void buildType(boolean isPermanent){
        this.isPermanent = isPermanent;
    }

    void buildPastMeeting(Integer prevMeeting){
        this.prevMeeting = prevMeeting;
    }

   Trade buildTrade() {
        if (isPermanent) {
            newTrade = new PermanentTrade(users, items, dateAndTime, location, prevMeeting, new PermanentTradingStrategy());
        } else {
            newTrade = new TemporaryTrade(users, items, dateAndTime, location, prevMeeting, new TemporaryTradingStrategy());
        }
        return newTrade;
    }
}
