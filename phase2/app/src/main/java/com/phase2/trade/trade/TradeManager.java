package main.java.com.phase2.trade.trade;

import main.java.com.phase2.trade.property.TradeProperties;
import main.java.com.phase2.trade.user.*;
import main.java.com.phase2.trade.repository.*;

import java.util.*;
import java.time.LocalDateTime;

class TradeManager {
    private final Integer editLimit;
    private final Integer timeLimit;
    private final Repository<Trade> tradeRepository;
    private final Repository<PersonalUser> userRepository;

    /**
     * Creates, confirms and edits trades
     * @param tradeRepository A repository of all the trades in the system
     * @param userRepository A repository of all users in the system
     * @param tradeProperties A place to store restrictions on a trade (e.g how many times a user can edit,
     *                        how many days until temporary trades must be reversed)
     */
    public TradeManager(Repository<Trade> tradeRepository, Repository<PersonalUser>
            userRepository, TradeProperties tradeProperties) {
        // Default Values for trade information:
        editLimit = tradeProperties.getInt("editLimit");
        timeLimit = tradeProperties.getInt("timeLimit");
        this.tradeRepository = tradeRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a trade meeting to trade back items
     * @param user1 The user ID of who initiated this trade
     * @param user2 The user ID of who the other trader
     * @param item1 The item ID of what the initiator plans to lend
     * @param item2 The item ID of what the initiator plans to borrow
     * @param isPermanent True iff this trade is permanent
     * @param dateAndTime The date and time that this trade will take place
     * @param location The location of where this trade will take place
     * @param prevMeeting The trade ID of the previous meeting
     */
    public void createTrade(int user1, int user2, Integer item1, Integer item2, Boolean isPermanent,
                                LocalDateTime dateAndTime, String location, Integer prevMeeting) {
        // Get users from Repository
        PersonalUser trader1 = userRepository.get(user1);
        PersonalUser trader2 = userRepository.get(user2);

        Trade newTrade = new Trade(user1, user2, item1, item2, isPermanent, dateAndTime, location,
                prevMeeting);
        tradeRepository.add(newTrade);
        trader1.addToTrades(newTrade.getUid());
        trader2.addToTrades(newTrade.getUid());
    }


    /**
     * Create a trade trade meeting to trade items
     * @param user1 The user ID of who initiated this trade
     * @param user2 The user ID of who the other trader
     * @param item1 The item ID of what the initiator plans to lend
     * @param item2 The item ID of what the initiator plans to borrow
     * @param isPermanent True iff this trade is permanent
     * @param dateAndTime The date and time that this trade will take place
     * @param location The location of where this trade will take place
     */
    public void createTrade(int user1, int user2, Integer item1, Integer item2, Boolean isPermanent,
                                LocalDateTime dateAndTime, String location){
        createTrade(user1, user2, item1, item2, isPermanent, dateAndTime, location, null);
    }

    /**
     * Edit date and time of a trade and cancels the trade if the users edited too much
     * @param tradeID The trade ID of the trade to be edited
     * @param editingUser The user ID of who wishes to edit this trade
     * @param dateAndTime The new date and time that this trade will take place
     */
    public void editDateAndTime(int tradeID, int editingUser, LocalDateTime dateAndTime) {
        // Get trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        if ((currTrade.getUser1() == editingUser || currTrade.getUser2() == editingUser) && currTrade.getUser1Edits() ==
                editLimit && currTrade.getUser2Edits() == editLimit) {
            cancelTrades(currTrade);
        } else if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms() && currTrade.getUser1Edits() <
                editLimit) {
            currTrade.setDateAndTime(dateAndTime);
            currTrade.increaseUser1Edits();
            currTrade.confirmUser1();
            currTrade.unconfirmUser2();
        } else if (currTrade.getUser2() == editingUser && !currTrade.getUser2Confirms() && currTrade.getUser2Edits() <
                editLimit) {
            currTrade.setDateAndTime(dateAndTime);
            currTrade.increaseUser2Edits();
            currTrade.unconfirmUser1();
            currTrade.confirmUser2();
        }
    }

    /**
     Edit location of a trade and cancels the trade if the users edited too much
     * @param tradeID The trade ID of the trade to be edited
     * @param editingUser The user ID of who wishes to edit this trade
     * @param location The new location of where this trade will take place
     */
    public void editLocation(int tradeID, int editingUser, String location) {
        // Get Trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Only unconfirmed parties can edit and users automatically confirm to their edit
        if ((currTrade.getUser1() == editingUser || currTrade.getUser2() == editingUser) && currTrade.getUser1Edits() ==
                editLimit && currTrade.getUser2Edits() == editLimit) {
            cancelTrades(currTrade);
        } else if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms() && currTrade.getUser1Edits() <
                editLimit) {
            currTrade.setLocation(location);
            currTrade.increaseUser1Edits();
            currTrade.confirmUser1();
            currTrade.unconfirmUser2();
        } else if (currTrade.getUser2() == editingUser && !currTrade.getUser2Confirms() && currTrade.getUser2Edits() <
                editLimit) {
            currTrade.setLocation(location);
            currTrade.increaseUser2Edits();
            currTrade.unconfirmUser1();
            currTrade.confirmUser2();
        }
    }

    // Removes a trade from user's trade list and the trade repository
    private void cancelTrades(Trade currTrade) {
        PersonalUser trader1 = userRepository.get(currTrade.getUser1());
        PersonalUser trader2 = userRepository.get(currTrade.getUser1());
        trader1.removeFromTrade(currTrade.getUid());
        trader2.removeFromTrade(currTrade.getUid());
        tradeRepository.remove(currTrade);
    }

    /**
     * Confirm a trade will take place and opens the trade
     * @param tradeID The trade ID of the trade to be confirmed
     * @param editingUser The user ID of who wishes to confirm to this trade
     */
    public void confirmTrade(int tradeID, int editingUser) {
        // Get Trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Confirm specific user
        if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms() && currTrade.getIsClosed()) {
            currTrade.confirmUser1();
        } else if (currTrade.getUser2() == editingUser && !currTrade.getUser2Confirms() && currTrade.getIsClosed()) {
            currTrade.confirmUser2();
        }
        openTrade(tradeID);
    }

    // Opens a trade and closes the previous trade if applicable
    private void openTrade(int tradeID){
        // Get trade from repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Open trade if both users confirm
        if (currTrade.getUser1Confirms() && currTrade.getUser2Confirms()) {
            currTrade.openTrade();
            currTrade.unconfirmUser1();
            currTrade.unconfirmUser2();

            // Close first meeting if this is a second meeting to trade back
            if (currTrade.getPrevMeeting() != null && tradeRepository.ifExists(currTrade.getPrevMeeting())) {
                Trade oldTrade = tradeRepository.get(currTrade.getPrevMeeting());
                oldTrade.closeTrade();
            }
        }
    }

    /**
     * Confirm that a trade has occurred and completes a trade by making the trades and closing it or scheduling another
     * meeting.
     * @param tradeID The trade ID of the trade to be confirmed
     * @param editingUser The user ID of who wishes to confirmed this trade
     */
    public void confirmTradeComplete(int tradeID, int editingUser) {
        // Get Trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Confirm specific user
        if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms() && !currTrade.getIsClosed()) {
            currTrade.confirmUser1();
        } else if (currTrade.getUser2() == editingUser && !currTrade.getUser2Confirms() && !currTrade.getIsClosed()) {
            currTrade.confirmUser2();
        }
        completeTrade(tradeID);
    }

    // Makes a one-way or two-way trade
    private void makeTrades(Trade currTrade) {
        PersonalUser initUser = userRepository.get(currTrade.getUser1());
        PersonalUser otherUser = userRepository.get(currTrade.getUser2());
        if (currTrade.getItem1() == null && currTrade.getItem2() != null) {
            initUser.removeFromWishList(currTrade.getItem2());
            otherUser.removeFromInventory(currTrade.getItem2());
            if (currTrade.getPrevMeeting() == null){
                initUser.setBorrowCount(initUser.getBorrowCount() + 1);
                otherUser.setLendCount(otherUser.getLendCount() + 1);
                initUser.addRecentTrades(currTrade.getUid());
                otherUser.addRecentTrades(currTrade.getUid());
                initUser.setNumTransactions(initUser.getNumTransactions() + 1);
                otherUser.setNumTransactions(otherUser.getNumTransactions() + 1);
            }
            if (currTrade.getIsPermanent()) {
                initUser.addToInventory(currTrade.getItem2());
            }
        } else if (currTrade.getItem2() == null && currTrade.getItem1() != null) {
            otherUser.removeFromWishList(currTrade.getItem1());
            initUser.removeFromInventory(currTrade.getItem1());
            if (currTrade.getPrevMeeting() == null){
                otherUser.setBorrowCount(otherUser.getBorrowCount() + 1);
                initUser.setLendCount(initUser.getLendCount() + 1);
                initUser.addRecentTrades(currTrade.getUid());
                otherUser.addRecentTrades(currTrade.getUid());
                initUser.setNumTransactions(initUser.getNumTransactions() + 1);
                otherUser.setNumTransactions(otherUser.getNumTransactions() + 1);
            }
            if (currTrade.getIsPermanent()) {
                otherUser.addToInventory(currTrade.getItem1());
            }
        } else {
            initUser.removeFromWishList(currTrade.getItem2());
            initUser.removeFromInventory(currTrade.getItem1());
            otherUser.removeFromWishList(currTrade.getItem1());
            otherUser.removeFromInventory(currTrade.getItem2());
            // itemManager.removeAvailable(currTrade.getItem1());
            // itemManager.removeAvailable(currTrade.getItem2());
            if (currTrade.getPrevMeeting() == null){
                initUser.setBorrowCount(initUser.getBorrowCount() + 1);
                initUser.setLendCount(initUser.getLendCount() + 1);
                otherUser.setLendCount(otherUser.getLendCount() + 1);
                otherUser.setBorrowCount(otherUser.getBorrowCount() + 1);
                initUser.addRecentTrades(currTrade.getUid());
                otherUser.addRecentTrades(currTrade.getUid());
                initUser.setNumTransactions(initUser.getNumTransactions() + 1);
                otherUser.setNumTransactions(otherUser.getNumTransactions() + 1);
            }
            if (currTrade.getIsPermanent()) {
                initUser.addToInventory(currTrade.getItem2());
                otherUser.addToInventory(currTrade.getItem1());
                // itemManager.addAvailable(currTrade.getItem1());
                // itemManager.addAvailable(currTrade.getItem2());
            }
        }
    }

    // Completes a trade by making trades or scheduling second meeting
    private void completeTrade(int tradeID) {
        // Get trade from repository
        Trade currTrade = tradeRepository.get(tradeID);

        // If both users confirm, make the trade
        if (currTrade.getUser1Confirms() && currTrade.getUser2Confirms()) {
            makeTrades(currTrade);
            if (currTrade.getIsPermanent()) {
                currTrade.closeTrade();
            } else {
                scheduleTradeBack(currTrade);
            }
        }
    }

    // Schedules a second trade meeting
    private void scheduleTradeBack(Trade currTrade) {
        LocalDateTime newDateAndTime = currTrade.getDateAndTime().plusMonths(timeLimit);
        createTrade(currTrade.getUser1(), currTrade.getUser1(), currTrade.getItem2(), currTrade.getItem1(),
                true, newDateAndTime, currTrade.getLocation(), currTrade.getUid());
    }


    /**
     * @param user The user ID
     * @return The frequency this user trades with other users
     */
    public Map<Integer, Integer> getTradeFrequency(int user) {
        Map<Integer, Integer> tradeFrequency = new HashMap<>();
        Iterator<Trade> tradeIterator = tradeRepository.iterator(entity -> entity.getUser1() == user ||
                entity.getUser2() == user);
        while (tradeIterator.hasNext()) {
            Trade trade = tradeIterator.next();
            if (user == trade.getUser1()) {
                putOrAppend(tradeFrequency, trade.getUser2());
            } else {
                putOrAppend(tradeFrequency, trade.getUser1());
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

    public void getRecentTrades(PersonalUser currUser){
        List<Integer> recentCompleteTrades = currUser.getRecentCompleteTrades();
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : recentCompleteTrades) {
            Trade trade = tradeRepository.get(i);
            stringBuilder.append(trade.toString()).append("\n");
        }
    }

    public void getAllTrades(PersonalUser currUser){
        List<Integer> allTrades = currUser.getTrades();
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : allTrades) {
            Trade trade = tradeRepository.get(i);
            stringBuilder.append(trade.toString()).append("\n");
        }
    }
}
