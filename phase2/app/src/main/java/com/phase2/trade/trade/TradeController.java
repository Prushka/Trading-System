package main.java.com.phase2.trade.trade;

import main.java.com.phase2.trade.property.TradeProperties;
import main.java.com.phase2.trade.user.*;
import main.java.com.phase2.trade.repository.*;

import java.util.*;
import java.time.LocalDateTime;

class TradeController {
    private TradeManager tradeManager;

    public TradeController(Repository<Trade> tradeRepository, Repository<PersonalUser>
            personalUserRepository, TradeProperties tradeProperties){
        tradeManager = new TradeManager(tradeRepository, personalUserRepository,
                tradeProperties);
    }

    public void addTrade(PersonalUser currUser, PersonalUser trader2, Integer lendingItem, Integer
                         borrowingItem, Boolean isPermanent, String dateInput, String location) {
        Integer item1;
        Integer item2;

        // Check if items are in their inventories or are null
        if (lendingItem.equals("null")) {
            item1 = null;
        } else if (currUser.getInventory().contains(lendingItem)) {
            item1 = lendingItem;
        } else {
            return;
        }

        if (borrowingItem.equals("null")) {
            item2 = null;
        } else if (trader2.getInventory().contains(borrowingItem)) {
            item2 = borrowingItem;
        } else {
            return;
        }

        String[] data = dateInput.split("-");
        LocalDateTime dateAndTime = LocalDateTime.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]));
        tradeManager.createTrade(currUser.getUid(), trader2.getUid(), item1, item2, isPermanent, dateAndTime, location);
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
}

