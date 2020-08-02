package main.java.phase2.trade.exclude.trade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
public class TradeBuilder {

    Integer numTraders;
    Boolean isPermanent;
    List<Integer> users;
    List<List<Integer>> items;
    LocalDateTime dateAndTime;
    String location;
    Integer prevMeeting;

    Trade newTrade;

    public TradeBuilder() {}

    public void buildAmountOfTraders(int num) {
        numTraders = num;
        users = new ArrayList<>(num);
        items = new ArrayList<>(num);
    }

    public void buildTraders(List<Integer> users) {
        for (Integer i: users){
         this.users.add(i);
        }
    }

    public void buildItems(List<List<Integer>> items){
        for (List<Integer> i: items){
            this.items.add(i);
        }
    }

    public void buildDateAndTime(LocalDateTime dateAndTime){
        this.dateAndTime = dateAndTime;
    }

    public void buildLocation(String location){
        this.location = location;
    }

    public void buildType(boolean isPermanent){
        this.isPermanent = isPermanent;
    }

    public void buildPastMeeting(Integer prevMeeting){
        this.prevMeeting = prevMeeting;
    }

    public Trade buildTrade() {
        if (isPermanent) {
            newTrade = new PermanentTrade(users, items, dateAndTime, location, prevMeeting, new PermanentTradingStrategy());
        } else {
            newTrade = new TemporaryTrade(users, items, dateAndTime, location, prevMeeting, new TemporaryTradingStrategy());
        }
        return newTrade;
    }
}
