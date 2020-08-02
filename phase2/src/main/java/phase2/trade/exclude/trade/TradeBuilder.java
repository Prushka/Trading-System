package main.java.phase2.trade.exclude.trade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TradeBuilder {

    Integer numTraders;

    List<Integer> users;
    List<Boolean> confirmations;
    List<Integer> edits;
    List<List<Integer>> items;
    Integer prevMeeting;
    Boolean isPermanent;
    LocalDateTime dateAndTime;
    String location;

    Trade newTrade;

    public TradeBuilder() {}

    public TradeBuilder buildAmountOfTraders(int num) {
        numTraders = num;
        users = new ArrayList<>(num);
        edits = new ArrayList<>(num);
        confirmations = new ArrayList<>(num);
        confirmations.set(0, true);
        items = new ArrayList<>(num);
        return this;
    }

    public TradeBuilder buildTraders(List<Integer> users) {
        for (Integer i: users){
         this.users.add(i);
        }
        return this;
    }

    public TradeBuilder buildItems(List<List<Integer>> items){
        for (List<Integer> i: items){
            this.items.add(i);
        }
        return this;
    }

    public TradeBuilder buildDateAndTime(LocalDateTime dateAndTime){
        this.dateAndTime = dateAndTime;
        return this;
    }

    public TradeBuilder buildLocation(String location){
        this.location = location;
        return this;
    }

    public TradeBuilder buildType(boolean isPermanent){
        this.isPermanent = isPermanent;
        return this;
    }

    public TradeBuilder buildPastMeeting(Integer prevMeeting){
        this.prevMeeting = prevMeeting;
        return this;
    }

    public Trade buildTrade() {
        if (isPermanent) {
            newTrade = new TemporaryTrade(order, bc, bl, bp);
        }else {
            newTrade = new PermanentTrade(order, bc, bl, bp);
        }
        return newTrade;
    }
}
