package group.trade;

import group.config.property.TradeProperties;
import group.item.Item;
import group.menu.data.Request;
import group.notification.SupportTicket;
import group.repository.Repository;
import group.repository.reflection.CSVMappable;
import group.repository.reflection.MappableBase;
import group.user.PersonalUser;
import group.menu.data.Response;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

// Remove prompts for user ID?, should be given because dont want to alter other people's accounts
// TODO: add validating existing in menu constructor/ filter response, better prompt for date, fix confirming problem
public class TradeManager {
    private final Integer editLimit; // how many edits each user has
    private final Integer timeLimit; // days until a user has to reverse the temporary trade
    private Repository<Trade> tradeRepository;
    private Repository<PersonalUser> userRepository;

    public TradeManager(Repository<Trade> tradeRepository, Repository<PersonalUser> userRepository, TradeProperties
            tradeProperties) {
        // Default Values for trade information stored in tradeProperties:
        tradeProperties.set("editLimit", "3");
        tradeProperties.set("timeLimit", "30");
        editLimit = Integer.parseInt(tradeProperties.get("editLimit"));
        timeLimit = Integer.parseInt(tradeProperties.get("timeLimit"));
        this.tradeRepository = tradeRepository;
        this.userRepository = userRepository;
    }

    public Response createTrade(long user1, long user2, long item1, long item2, Boolean isPermanent,
                             Date dateAndTime, String location, Long prevMeeting) {
        // TODO: Move conditions to controller or fix them here
        // TODO: Uncomment conditions when user's are implemented in the controller
        // Get User from Repository
        // if (userRepository.ifExists(user1) && userRepository.ifExists(user2)) {
            //PersonalUser trader1 = userRepository.get(user1);
            //PersonalUser trader2 = userRepository.get(user2);
            // Check if items are in their inventories
            //if ((item1.equals(null) || trader1.getInventory().contains(item1)) && (item2.equals(null) ||
              //      trader2.getInventory().contains(item2))) {
                Trade newTrade = new Trade(user1, user2, item1, item2, isPermanent,
                        dateAndTime, location, prevMeeting);
                tradeRepository.add(newTrade);
                return tradeRepresentation(newTrade);
            //}
        //}
        //return new Response.Builder(false).translatable("failed.create.trade").build();
    }

    // Overloaded constructor to differentiate first and second meetings
    public Response createTrade(long user1, long user2, long item1, long item2, Boolean isPermanent,
                                Date dateAndTime, String location){
        return createTrade(user1, user2, item1, item2, isPermanent, dateAndTime, location, null);
    }

    public Response editDateAndTime(int tradeID, int editingUser, Date dateAndTime) {
        // Get trade from Repository
        if (tradeRepository.ifExists(tradeID)) {
            Trade currTrade = tradeRepository.get(tradeID);

            // Only unconfirmed parties can edit and users automatically confirm to their edit
            if (currTrade.getUser1Edits() == editLimit && currTrade.getUser2Edits() == editLimit) {
                tradeRepository.remove(currTrade);
                return new Response.Builder(false).translatable("failed.cancel.trade").build();
            } else if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms() &&
                    currTrade.getUser1Edits() < editLimit) {
                currTrade.setDateAndTime(dateAndTime);
                currTrade.increaseUser1Edits();
                currTrade.confirmUser1();
                currTrade.unconfirmUser2();
            } else if (currTrade.getUser2() == editingUser && !currTrade.getUser2Confirms() &&
                    currTrade.getUser2Edits() < editLimit) {
                currTrade.setDateAndTime(dateAndTime);
                currTrade.increaseUser2Edits();
                currTrade.unconfirmUser1();
                currTrade.confirmUser2();
            }
            return tradeRepresentation(currTrade);
        }
        return new Response.Builder(false).translatable("failed.edit.trade").build();
    }

    public Response editLocation(int tradeID, int editingUser, String location) {
        // Get Trade from Repository
        if (tradeRepository.ifExists(tradeID)) {
            Trade currTrade = tradeRepository.get(tradeID);

            // Only unconfirmed parties can edit and users automatically confirm to their edit
            if (currTrade.getUser1Edits() == editLimit && currTrade.getUser2Edits() == editLimit) {
                tradeRepository.remove(currTrade);
                return new Response.Builder(false).translatable("failed.cancel.trade").build();
            } else if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms() &&
                    currTrade.getUser1Edits() < editLimit) {
                currTrade.setLocation(location);
                currTrade.increaseUser1Edits();
                currTrade.confirmUser1();
                currTrade.unconfirmUser2();
            } else if (currTrade.getUser2() == editingUser && !currTrade.getUser2Confirms() &&
                    currTrade.getUser2Edits() < editLimit) {
                currTrade.setLocation(location);
                currTrade.increaseUser2Edits();
                currTrade.unconfirmUser1();
                currTrade.confirmUser2();
            }
            return tradeRepresentation(currTrade);
        }
        return new Response.Builder(false).translatable("failed.edit.trade").build();
    }

    public Response confirmTrade(int tradeID, int editingUser) {
        // Get Trade from Repository
        if (tradeRepository.ifExists(tradeID)) {
            Trade currTrade = tradeRepository.get(tradeID);

            // Confirm specific user
            if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms() && currTrade.getIsClosed()) {
                currTrade.confirmUser1();
            } else if (currTrade.getUser2() == editingUser && !currTrade.getUser1Confirms() && currTrade.getIsClosed()) {
                currTrade.confirmUser2();
            } else {
                return new Response.Builder(false).translatable("failed.confirm.trade").build();
            }

            // Open trade if both users confirm
            if (currTrade.getUser1Confirms() && currTrade.getUser2Confirms()) {
                currTrade.openTrade();
                currTrade.unconfirmUser1();
                currTrade.unconfirmUser2();

                // Close first meeting if this is a second meeting to trade back
                long oldMeeting = currTrade.getPrevMeeting();
                if (tradeRepository.ifExists(oldMeeting)) {
                    Trade oldTrade = tradeRepository.get(oldMeeting);
                    oldTrade.closeTrade();
                }
                return new Response.Builder(true).translatable("success.confirm.trade.open").build();
            } else {
                return new Response.Builder(true).translatable("success.confirm.trade.wait").build();
            }
        }
        return new Response.Builder(false).translatable("failed.confirm.trade").build();
    }

    public Response confirmTradeComplete(int tradeID, int editingUser) {
        // Get Trade from Repository
        if (userRepository.ifExists(editingUser) && tradeRepository.ifExists(tradeID)) {
            PersonalUser currUser = userRepository.get(editingUser);
            Trade currTrade = tradeRepository.get(tradeID);

            // Confirm specific user
            if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms()) {
                currTrade.confirmUser1();
                PersonalUser otherUser = userRepository.get(currTrade.getUser2());
                if (currTrade.getUser1Confirms() && currTrade.getUser2Confirms()) {
                    makeTrades(currUser, otherUser, currTrade);
                }
            } else if (currTrade.getUser2() == editingUser && !currTrade.getUser1Confirms()) {
                currTrade.confirmUser2();
                PersonalUser otherUser = userRepository.get(currTrade.getUser1());
                if (currTrade.getUser1Confirms() && currTrade.getUser2Confirms()) {
                    makeTrades(currUser, otherUser, currTrade);
                    if (currTrade.getIsPermanent()) {
                        currTrade.closeTrade();
                    } else {
                        scheduleTradeBack(currTrade);
                    }
                }
                return new Response.Builder(true).translatable("success.confirm.trade.complete").build();
            }
            return new Response.Builder(true).translatable("success.confirm.trade.wait").build();
        }
        return new Response.Builder(false).translatable("failed.confirm.trade").build();
    }

    // TODO: Add to other people's inventory -- need item repo?
    private void makeTrades(PersonalUser currUser, PersonalUser otherUser, Trade currTrade) {
        if (currTrade.getItem1() == null && currTrade.getItem2() != null) {
            currUser.setBorrowCount(currUser.getBorrowCount() + 1);
            currUser.removeFromWishList(currTrade.getItem2());
            otherUser.setLendCount(currUser.getLendCount() + 1);
            otherUser.removeFromInventory(currTrade.getItem2());
            if (currTrade.getIsPermanent()) {
                // currUser.addToInventory(currTrade.getItem2());
            }
        } else if (currTrade.getItem2() == null && currTrade.getItem1() != null) {
            otherUser.setBorrowCount(currUser.getBorrowCount() + 1);
            otherUser.removeFromWishList(currTrade.getItem1());
            currUser.setLendCount(currUser.getLendCount() + 1);
            currUser.removeFromInventory(currTrade.getItem1());
            if (currTrade.getIsPermanent()) {
                // otherUser.addToInventory(currTrade.getItem2());
            }
        } else {
            currUser.setBorrowCount(currUser.getBorrowCount() + 1);
            currUser.setLendCount(currUser.getLendCount() + 1);
            currUser.removeFromWishList(currTrade.getItem2());
            currUser.removeFromInventory(currTrade.getItem1());
            otherUser.setLendCount(currUser.getLendCount() + 1);
            otherUser.setBorrowCount(currUser.getBorrowCount() + 1);
            otherUser.removeFromWishList(currTrade.getItem1());
            otherUser.removeFromInventory(currTrade.getItem2());
            if (currTrade.getIsPermanent()) {
                // currUser.addToInventory(currTrade.getItem2());
                // otherUser.addToInventory(currTrade.getItem1());
            }
        }
    }

    private void scheduleTradeBack(Trade currTrade) {
        long increaseTime = currTrade.getDateAndTime().getTime() + (timeLimit * 86400000);
        Date newDateAndTime = new Date(increaseTime);
        createTrade(currTrade.getUser1(), currTrade.getUser1(), currTrade.getItem2(), currTrade.getItem1(),
                true, newDateAndTime, currTrade.getLocation(), currTrade.getUid());
    }


    /**
     * @param trade A trade to be converted
     * @return A response object that corresponds with a string representation of a Trade
     */
    private Response tradeRepresentation(Trade trade) {
        return new Response.Builder(true).
                translatable("submit.trade.represent", trade.getUid(), trade.getUser1(), trade.getUser2(),
                        trade.getIsPermanent(), trade.getDateAndTime(), trade.getLocation())
                .build();
    }
}

