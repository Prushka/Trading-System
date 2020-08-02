package main.java.phase2.trade.exclude.trade;

import main.java.com.phase2.trade.user.*;
import main.java.com.phase2.trade.item.*;
import main.java.com.phase2.trade.repository.*;

import java.time.DateTimeException;
import java.util.*;
import java.time.LocalDateTime;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
class TradeController {
    private TradeManager tradeManager;

    public TradeController(Repository<Trade> tradeRepository, Repository<PersonalUser>
            personalUserRepository, Repository<Item> itemRepository){
        tradeManager = new TradeManager(tradeRepository, personalUserRepository, itemRepository);
    }

    public void addTrade(List<Integer> users, List<List<Integer>> items, String dateInput, String
            location) {

        String[] data = dateInput.split("-");
        LocalDateTime dateAndTime = LocalDateTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]));
        tradeManager.createTrade(users, items, dateAndTime, location, spec);
    }

    public void editMeetingDateAndTime(PersonalUser currUser, Integer tradeID, String dateInput){
        String[] data = dateInput.split("-");
        LocalDateTime dateAndTime = LocalDateTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]));
        tradeManager.editDateAndTime(tradeID, currUser.getUid(), dateAndTime);
    }


    public void editMeetingLocation(PersonalUser currUser, Integer tradeID, String location){
        tradeManager.editLocation(tradeID, currUser.getUid(), location);
    }

    public void confirmingTradeOpen(PersonalUser currUser, Integer tradeID){
        tradeManager.confirmTrade(tradeID, currUser.getUid());
    }

    public void confirmingTradeComplete(PersonalUser currUser, Integer tradeID){
        tradeManager.confirmTradeComplete(tradeID, currUser.getUid());
    }

    findByLocation(){

    }

    public boolean isBool(String input){
        return (input.equals("true") || input.equals("false"));
    }

    public boolean isAnItem(String input){
        try {
            if (input.equals("null")){ // || new RepositoryIdValidator(itemRepository).validate(input)
                return true;
            }
            return true;
        } catch (IllegalArgumentException e){
            return false;
        }
    }

    public Map<Integer, Integer> getTradeFrequency(int user) {
        return tradeManager.getTradeFrequency(user);
    }

    public boolean validate(String input) {
        String[] data = input.split("-");
        if (data.length == 5){
            try {
                int year = Integer.parseInt(data[0]);
                int month = Integer.parseInt(data[1]);
                int day = Integer.parseInt(data[2]);
                int hour = Integer.parseInt(data[3]);
                int minute = Integer.parseInt(data[4]);
                return (LocalDateTime.of(year, month, day, hour, minute)).isAfter(LocalDateTime.now());
            } catch (NumberFormatException | DateTimeException e) { // is this allowed
                return false;
            }
        }
        return false;
    }

    /**
     * @param user The user ID
     * @return The frequency this user trades with other users
     */
    Map<Integer, Integer> getTradeFrequency(int user) {
        Map<Integer, Integer> tradeFrequency = new HashMap<>();
        Iterator<Trade> tradeIterator = tradeRepository.iterator(entity -> entity.getAllUsers().contains(user));
        while (tradeIterator.hasNext()) {
            Trade trade = tradeIterator.next();
            for (int i: trade.getAllUsers()){
                if (user != i){
                    putOrAppend(tradeFrequency, i);
                }
            }
        }
        return tradeFrequency;
    }

    private void putOrAppend(Map<Integer, Integer> map, Integer key) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, 1);
        }
    }

    /**
     * @param currUser The current user being examined
     */
    void getRecentTrades(PersonalUser currUser){
        List<Integer> recentCompleteTrades = currUser.getRecentCompleteTrades();
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : recentCompleteTrades) {
            Trade trade = tradeRepository.get(i);
            stringBuilder.append(trade.toString()).append("\n");
        }
    }

    /**
     * @param currUser The current user being examined
     */
    void getAllTrades(PersonalUser currUser){
        List<Integer> allTrades = currUser.getTrades();
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : allTrades) {
            Trade trade = tradeRepository.get(i);
            stringBuilder.append(trade.toString()).append("\n");
        }
    }
}

