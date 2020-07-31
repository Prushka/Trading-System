package com.phase2.trade.trade;

import main.java.com.phase2.trade.user.*;
import main.java.com.phase2.trade.item.*;
import main.java.com.phase2.trade.repository.*;

import java.util.*;
import java.time.LocalDateTime;

class TradeManager {
    private Integer editLimit = 3;
    private Integer timeLimit = 1;
    private final Repository<Trade> tradeRepository;
    private final Repository<PersonalUser> userRepository;
    private final Repository<Item> itemRepository;

    /**
     * Creates, confirms and edits trades
     * @param tradeRepository A repository of all the trades in the system
     * @param userRepository A repository of all users in the system
     */
    TradeManager(Repository<Trade> tradeRepository, Repository<PersonalUser>
            userRepository, Repository<Item> itemRepository) {
        // editLimit = tradeProperties.getInt("editLimit");
        // timeLimit = tradeProperties.getInt("timeLimit");
        this.tradeRepository = tradeRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    /**
     * Creates a trade meeting to trade items temporarily
     * @param users All the userID's associated with this trade
     * @param items All the items associated with this trade. Each list corresponds to the desired
     *              items of the userID in users with the same index
     * @param dateAndTime When this trade takes place
     * @param location Where this trade takes place
     */
    void createTrade(List<Integer> users, List<List<Integer>> items, LocalDateTime
            dateAndTime, String location, TradeSpec spec) {

        Trade newTrade;
        switch (spec){
            case TEMPORARY:
                newTrade = new TemporaryTrade(users, items, dateAndTime, location);
            case PERMANENT:
                newTrade = new PermanentTrade(users, items, dateAndTime, location);
            default: newTrade = new Trade(new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), "");
        }

        tradeRepository.add(newTrade);
        for (int i: users){
            PersonalUser user = userRepository.get(i);
            user.addToTrades(newTrade.getUid());
        }
    }


    /**
     * Creates a trade meeting to trade items temporarily
     * @param users All the userID's associated with this trade
     * @param items All the items associated with this trade. Each list corresponds to the desired
     *              items of the userID in users with the same index
     * @param dateAndTime When this trade takes place
     * @param location Where this trade takes place
     * @param prevMeeting The tradeID of the previous meeting
     */
    void createTrade(List<Integer> users, List<List<Integer>> items, LocalDateTime
            dateAndTime, String location, Integer prevMeeting) {
        Trade newTrade = new ReturnTrade(users, items, dateAndTime, location, prevMeeting);
        tradeRepository.add(newTrade);
        for (int i: users){
            PersonalUser user = userRepository.get(i);
            user.addToTrades(newTrade.getUid());
        }
    }

    /**
     * Edit date and time of a trade and cancels the trade if the users edited too much
     * @param tradeID The trade ID of the trade to be edited
     * @param editingUser The user ID of who wishes to edit this trade
     * @param dateAndTime The new date and time that this trade will take place
     */
    void editDateAndTime(int tradeID, int editingUser, LocalDateTime dateAndTime) {
        // Get trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        if ((currTrade.getAllUsers().contains(editingUser)) && currTrade.getUserEdits(editingUser)
                == editLimit) {
            cancelTrades(currTrade);
        } else if (currTrade.getAllUsers().contains(editingUser) &&
                !currTrade.getUserConfirms(editingUser) && currTrade.getUserEdits(editingUser) <
                editLimit) {
            currTrade.setDateAndTime(dateAndTime);
            currTrade.increaseUserEdits(editingUser);
            currTrade.confirmUser(editingUser);
            for (int i: currTrade.getAllUsers()){
                if (i != editingUser) {
                    currTrade.unconfirmUser(i);
                }
            }
        }
    }

    /**
     Edit location of a trade and cancels the trade if the users edited too much
     * @param tradeID The trade ID of the trade to be edited
     * @param editingUser The user ID of who wishes to edit this trade
     * @param location The new location of where this trade will take place
     */
    void editLocation(int tradeID, int editingUser, String location) {
        // Get trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        if ((currTrade.getAllUsers().contains(editingUser)) && currTrade.getUserEdits(editingUser)
                == editLimit) {
            cancelTrades(currTrade);
        } else if (currTrade.getAllUsers().contains(editingUser) &&
                !currTrade.getUserConfirms(editingUser) && currTrade.getUserEdits(editingUser) <
                editLimit) {
            currTrade.setLocation(location);
            currTrade.increaseUserEdits(editingUser);
            currTrade.confirmUser(editingUser);
            for (int i: currTrade.getAllUsers()){
                if (i != editingUser) {
                    currTrade.unconfirmUser(i);
                }
            }
        }
    }

    // Removes a trade from user's trade list and the trade repository
    private void cancelTrades(Trade currTrade) {
        for (int i: currTrade.getAllUsers()){
            PersonalUser user = userRepository.get(i);
            user.removeFromTrade(currTrade.getUid());
        }
        tradeRepository.remove(currTrade);
    }

    /**
     * Confirm a trade will take place and opens the trade
     * @param tradeID The trade ID of the trade to be confirmed
     * @param editingUser The user ID of who wishes to confirm to this trade
     */
    void confirmTrade(int tradeID, int editingUser) {
        // Get Trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Confirm specific user
        if (currTrade.getAllUsers().contains(editingUser) && !currTrade.getUserConfirms(editingUser)
                && currTrade.getIsClosed()) {
            currTrade.confirmUser(editingUser);
        }
        openTrade(tradeID);
    }

    // Opens a trade and closes the previous trade if applicable
    private void openTrade(int tradeID){
        // Get trade from repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Check that all users confirm
        for (Boolean i: currTrade.getAllConfirmations()){
            if (i.equals(false)){
                return;
            }
        }

        // Opens trade and unconfirms users
        currTrade.openTrade();
        for (int i: currTrade.getAllUsers()){
            currTrade.unconfirmUser(i);
        }

        // Close first meeting if this is a second meeting to trade back
        if (currTrade instanceof ReturnTrade && tradeRepository.ifExists(((ReturnTrade)currTrade).getPrevMeeting())) {
            Trade oldTrade = tradeRepository.get(((ReturnTrade)currTrade).getPrevMeeting());
            oldTrade.closeTrade();
        }
    }

    /**
     * Confirm that a trade has occurred and completes a trade by making the trades and closing it or scheduling another
     * meeting.
     * @param tradeID The trade ID of the trade to be confirmed
     * @param editingUser The user ID of who wishes to confirmed this trade
     */
    void confirmTradeComplete(int tradeID, int editingUser) {
        // Get Trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Confirm specific user
        if (currTrade.getAllUsers().contains(editingUser) && !currTrade.getUserConfirms(editingUser)
                && currTrade.getIsClosed()) {
            currTrade.confirmUser(editingUser);
        }
        completeTrade(tradeID);
    }

    // Completes a trade by making trades or scheduling second meeting
    private void completeTrade(int tradeID) {
        // Get trade from repository
        Trade currTrade = tradeRepository.get(tradeID);

        // If all users confirm, make the trade
        for (Boolean i: currTrade.getAllConfirmations()){
            if (i.equals(false)){
                return;
            }
        }
        makeTrades(currTrade);
        if (currTrade instanceof TemporaryTrade){
            scheduleTradeBack(currTrade);
        } else {
            currTrade.closeTrade();
        }
    }

    // Makes a one-way or two-way trade
    private void makeTrades(Trade currTrade) {
        List<Integer> lenders = new ArrayList<>();
        int index = 0;
        while (index < currTrade.getAllUsers().size()){

            // Get a user and their respective desired items list
            PersonalUser user = userRepository.get(currTrade.getAllUsers().get(index));
            List<Integer> items = currTrade.getAllItems().get(index);

            // Update that they traded
            if (!(currTrade instanceof ReturnTrade)){
                user.addRecentTrades(currTrade.getUid());
                user.setNumTransactions(user.getNumTransactions() + 1);

                // Update borrowing count
                if (!items.isEmpty()){
                    user.setBorrowCount(user.getBorrowCount() + 1);
                }
            }

            // Changes item availability and item owner name
            for (int item: items){
                Item currItem = itemRepository.get(item);
                user.removeFromWishList(item);
                if (currTrade instanceof PermanentTrade){
                    currItem.setIsAvailable(true);
                    currItem.setOwnerUsername(user.getUid());
                } else if (currTrade instanceof  TemporaryTrade){
                    currItem.setIsAvailable(false);
                } else {
                    currItem.setIsAvailable(true);
                }
                if (!(currTrade instanceof ReturnTrade) && !lenders.contains(currItem.getOwner())){
                    lenders.add(currItem.getOwner());
                }
            }
        }

        // Update lending count
        for (int i: lenders){
            PersonalUser lender = userRepository.get(i);
            lender.setLendCount(lender.getLendCount() + 1);
        }
    }

    // Schedules a second trade meeting
    private void scheduleTradeBack(Trade currTrade) {
        LocalDateTime newDateAndTime = currTrade.getDateAndTime().plusMonths(timeLimit);
        createTrade(currTrade.getAllUsers(), currTrade.getAllItems(), newDateAndTime,
                currTrade.getLocation(), currTrade.getUid());
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
