package main.java.phase2.trade.exclude.trade;

import java.util.*;
import java.time.LocalDateTime;

abstract class TradeManager {
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
     * @param prevMeeting The tradeID of the previous meeting
     */
    void createTrade(int num, List<Integer> users, List<List<Integer>> items, LocalDateTime
            dateAndTime, String location, Integer prevMeeting) {
        TradeBuilder tb = new TradeBuilder();
        Trade newTrade = tb.buildAmountOfTraders(num).buildTraders(users).buildItems(items).buildDateAndTime(dateAndTime).buildLocation(location).buildType(true).buildTrade();
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
        if ((currTrade.getAllUsers().contains(editingUser)) && currTrade.getUserEdits(editingUser) == editLimit) {
            cancelTrades(currTrade);
        } else if (currTrade.getAllUsers().contains(editingUser) && !currTrade.getUserConfirms(editingUser) && currTrade.getUserEdits(editingUser) < editLimit) {
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
        if ((currTrade.getAllUsers().contains(editingUser)) && currTrade.getUserEdits(editingUser) == editLimit) {
            cancelTrades(currTrade);
        } else if (currTrade.getAllUsers().contains(editingUser) && !currTrade.getUserConfirms(editingUser) && currTrade.getUserEdits(editingUser) < editLimit) {
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
            if (currTrade.getIsClosed()){
                openTrade(tradeID);
            } else {
                completeTrade(tradeID);
            }
        }
    }

    abstract void openTrade(int tradeID);
    abstract void completeTrade(int tradeID);


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
