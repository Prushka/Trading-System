package group.trade;

import group.config.property.TradeProperties;
import group.item.ItemManager;
import group.repository.Repository;
import group.user.PersonalUser;
import group.menu.data.Response;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TradeManager {
    private final Integer editLimit;
    private final Integer timeLimit;
    private final Repository<Trade> tradeRepository;
    private final Repository<PersonalUser> userRepository;
    private final ItemManager itemManager;

    /**
     * Creates, confirms and edits trades
     * @param tradeRepository A repository of all the trades in the system
     * @param userRepository A repository of all users in the system
     * @param tradeProperties A place to store restrictions on a trade (e.g how many times a user can edit,
     *                        how many days until temporary trades must be reversed)
     */
    public TradeManager(Repository<Trade> tradeRepository, Repository<PersonalUser> userRepository, TradeProperties
            tradeProperties, ItemManager itemManager) {
        // Default Values for trade information stored in tradeProperties:
        editLimit = Integer.parseInt(tradeProperties.get("editLimit"));
        timeLimit = Integer.parseInt(tradeProperties.get("timeLimit"));
        this.tradeRepository = tradeRepository;
        this.userRepository = userRepository;
        this.itemManager = itemManager;
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
     * @return A response object of the representation of the trade or a description of why creation failed
     */
    public Response createTrade(int user1, int user2, int item1, int item2, Boolean isPermanent,
                                LocalDateTime dateAndTime, String location, Integer prevMeeting) {
        // Get users from Repository
        PersonalUser trader1 = userRepository.get(user1);
        PersonalUser trader2 = userRepository.get(user2);

        Trade newTrade = new Trade(user1, user2, item1, item2, isPermanent, dateAndTime, location, prevMeeting);
        tradeRepository.add(newTrade);
        trader1.addToTrades(newTrade.getUid());
        trader2.addToTrades(newTrade.getUid());
        return tradeRepresentation(newTrade);
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
     * @return A response object of the representation of the trade or a description of why creation failed
     */
    public Response createTrade(int user1, int user2, int item1, int item2, Boolean isPermanent,
                                LocalDateTime dateAndTime, String location){
        return createTrade(user1, user2, item1, item2, isPermanent, dateAndTime, location, null);
    }

    /**
     * Edit date and time of a trade and cancels the trade if the users edited too much
     * @param tradeID The trade ID of the trade to be edited
     * @param editingUser The user ID of who wishes to edit this trade
     * @param dateAndTime The new date and time that this trade will take place
     * @return A response object of the representation of the new trade or a description of why creation failed
     */
    public Response editDateAndTime(int tradeID, int editingUser, LocalDateTime dateAndTime) {
        // Get trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        if ((currTrade.getUser1() == editingUser || currTrade.getUser2() == editingUser) && currTrade.getUser1Edits() ==
                editLimit && currTrade.getUser2Edits() == editLimit) {
            return cancelTrades(currTrade);
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
        } else {
            return new Response.Builder(false).translatable("failed.edit.trade").build();
        }
        return tradeRepresentation(currTrade);
    }

    /**
     Edit location of a trade and cancels the trade if the users edited too much
     * @param tradeID The trade ID of the trade to be edited
     * @param editingUser The user ID of who wishes to edit this trade
     * @param location The new location of where this trade will take place
     * @return A response object of the representation of the new trade or a description of why creation failed
     */
    public Response editLocation(int tradeID, int editingUser, String location) {
        // Get Trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Only unconfirmed parties can edit and users automatically confirm to their edit
        if ((currTrade.getUser1() == editingUser || currTrade.getUser2() == editingUser) && currTrade.getUser1Edits() ==
                editLimit && currTrade.getUser2Edits() == editLimit) {
            return cancelTrades(currTrade);
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
        } else {
            return new Response.Builder(false).translatable("failed.edit.trade").build();
        }
        return tradeRepresentation(currTrade);
    }

    // Removes a trade from user's trade list and the trade repository
    private Response cancelTrades(Trade currTrade) {
        PersonalUser trader1 = userRepository.get(currTrade.getUser1());
        PersonalUser trader2 = userRepository.get(currTrade.getUser1());
        trader1.removeFromTrade(currTrade.getUid());
        trader2.removeFromTrade(currTrade.getUid());
        tradeRepository.remove(currTrade);
        return new Response.Builder(false).translatable("failed.cancel.trade").build();
    }

    /**
     * Confirm a trade will take place and opens the trade
     * @param tradeID The trade ID of the trade to be confirmed
     * @param editingUser The user ID of who wishes to confirm to this trade
     * @return A response object that details the success or failure of this action
     */
    public Response confirmTrade(int tradeID, int editingUser) {
        // Get Trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Confirm specific user
        if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms() && currTrade.getIsClosed()) {
            currTrade.confirmUser1();
        } else if (currTrade.getUser2() == editingUser && !currTrade.getUser2Confirms() && currTrade.getIsClosed()) {
            currTrade.confirmUser2();
        } else {
            return new Response.Builder(false).translatable("failed.confirm.trade").build();
        }
        return openTrade(tradeID);
    }

    // Opens a trade and closes the previous trade if applicable
    private Response openTrade(int tradeID){
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
            return new Response.Builder(true).translatable("success.confirm.trade.open").build();
        } else {
            return new Response.Builder(true).translatable("success.confirm.trade.wait").build();
        }
    }

    /**
     * Confirm that a trade has occurred and completes a trade by making the trades and closing it or scheduling another
     * meeting.
     * @param tradeID The trade ID of the trade to be confirmed
     * @param editingUser The user ID of who wishes to confirmed this trade
     * @return A response object that details the success or failure of this action
     */
    public Response confirmTradeComplete(int tradeID, int editingUser) {
        // Get Trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Confirm specific user
        if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms() && !currTrade.getIsClosed()) {
            currTrade.confirmUser1();
        } else if (currTrade.getUser2() == editingUser && !currTrade.getUser2Confirms() && !currTrade.getIsClosed()) {
            currTrade.confirmUser2();
        } else {
            return new Response.Builder(false).translatable("failed.confirm.trade").build();
        }
        return completeTrade(tradeID);
    }

    // Makes a one-way or two-way trade
    private void makeTrades(Trade currTrade) {
        PersonalUser initUser = userRepository.get(currTrade.getUser1());
        PersonalUser otherUser = userRepository.get(currTrade.getUser2());
        if (currTrade.getItem1() == null && currTrade.getItem2() != null) {
            initUser.setBorrowCount(initUser.getBorrowCount() + 1);
            initUser.removeFromWishList(currTrade.getItem2());
            otherUser.setLendCount(initUser.getLendCount() + 1);
            otherUser.removeFromInventory(currTrade.getItem2());
            if (currTrade.getIsPermanent()) {
                initUser.addToInventory(currTrade.getItem2());
            }
        } else if (currTrade.getItem2() == null && currTrade.getItem1() != null) {
            otherUser.setBorrowCount(initUser.getBorrowCount() + 1);
            otherUser.removeFromWishList(currTrade.getItem1());
            initUser.setLendCount(initUser.getLendCount() + 1);
            initUser.removeFromInventory(currTrade.getItem1());
            if (currTrade.getIsPermanent()) {
                otherUser.addToInventory(currTrade.getItem1());
            }
        } else {
            initUser.setBorrowCount(initUser.getBorrowCount() + 1);
            initUser.setLendCount(initUser.getLendCount() + 1);
            initUser.removeFromWishList(currTrade.getItem2());
            initUser.removeFromInventory(currTrade.getItem1());
            otherUser.setLendCount(initUser.getLendCount() + 1);
            otherUser.setBorrowCount(initUser.getBorrowCount() + 1);
            otherUser.removeFromWishList(currTrade.getItem1());
            otherUser.removeFromInventory(currTrade.getItem2());
            itemManager.removeAvailable(currTrade.getItem1());
            itemManager.removeAvailable(currTrade.getItem2());
            if (currTrade.getIsPermanent()) {
                initUser.addToInventory(currTrade.getItem2());
                otherUser.addToInventory(currTrade.getItem1());
                itemManager.addAvailable(currTrade.getItem1());
                itemManager.addAvailable(currTrade.getItem2());
            }
        }

        // Updates recent trades and trader frequency
        initUser.addRecentTrades(currTrade.getUid());
        otherUser.addRecentTrades(currTrade.getUid());
        // initUser.setTraderFrequency(otherUser.getUid());
        // otherUser.setTraderFrequency(initUser.getUid());
    }

    // Completes a trade by making trades or scheduling second meeting
    private Response completeTrade(int tradeID) {
        // Get trade from repository
        Trade currTrade = tradeRepository.get(tradeID);

        // If both users confirm, make the trade
        if (currTrade.getUser1Confirms() && currTrade.getUser2Confirms()) {
            makeTrades(currTrade);
            if (currTrade.getIsPermanent()) {
                currTrade.closeTrade();
                return new Response.Builder(true).translatable("success.confirm.trade.complete.perm").build();
            } else {
                scheduleTradeBack(currTrade);
                return new Response.Builder(true).translatable("success.confirm.trade.complete.temp").build();
            }
        } else {
            return new Response.Builder(true).translatable("success.confirm.trade.wait").build();
        }
    }

    // Schedules a second trade meeting
    private void scheduleTradeBack(Trade currTrade) {
        LocalDateTime newDateAndTime = currTrade.getDateAndTime().plusMonths(timeLimit);
        createTrade(currTrade.getUser1(), currTrade.getUser1(), currTrade.getItem2(), currTrade.getItem1(),
                true, newDateAndTime, currTrade.getLocation(), currTrade.getUid());
    }

    // Represents a trade in a string
    private Response tradeRepresentation(Trade trade) {
        return new Response.Builder(true).
                translatable("submit.trade.represent", trade.getUid(), trade.getUser1(),
                        trade.getUser2(), trade.getIsPermanent(), trade.getDateAndTime(), trade.getLocation()).build();
    }


    public Map<Integer, Integer> getTradeFrequency(int user) {
        Map<Integer, Integer> tradeFrequency = new HashMap<>();
        Iterator<Trade> tradeIterator = tradeRepository.iterator(entity -> entity.getUser1() == user || entity.getUser2() == user);
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
}

