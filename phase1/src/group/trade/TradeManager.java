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

public class TradeManager {
    private final Integer editLimit;
    private final Integer timeLimit; // the number of miliseconds until a user has to reverse the temporary trade
    private Repository<Trade> tradeRepository;
    private Repository<PersonalUser> userRepository;

    public TradeManager(Repository<Trade> tradeRepository, Repository<PersonalUser> userRepository, TradeProperties
            tradeProperties) {
        // Default Values for trade information stored in tradeProperties:
        tradeProperties.set("editLimit", "3");
        tradeProperties.set("timeLimit", "1");
        editLimit = Integer.parseInt(tradeProperties.get("editLimit"));
        timeLimit = Integer.parseInt(tradeProperties.get("timeLimit"));
        this.tradeRepository = tradeRepository;
        this.userRepository = userRepository;

        /*
        // Get from Repository -- use for only one record
        Trade getSomeTrade = tradeRepository.getFirst(entity -> entity.getItem1() == null);
        Iterator<Trade> getAnIterator = tradeRepository.iterator(entity -> entity.getItem1() == null);
        Iterator<Trade> getAnIterator2 = tradeRepository.iterator(entity -> {
            if (entity.getUser1Confirms()) {

            } else if (entity.getUser2Confirms()) {

            }
            return false;
        });
        Trade getSomeTradeByUID = tradeRepository.get(10);

        // If entity exists in Repository
        boolean ifSomeTradeExists = tradeRepository.ifExists(entity -> entity.getDateAndTime() == null);
        boolean ifSomeTradeExists2 = tradeRepository.ifExists(4);
        boolean ifSomeTradeExists3 = tradeRepository.ifExists(new Trade()); Implement the equals() and hashCode() in Trade to use this one

        // TODO: Response
        Response response = tradeRepository.filterResponse(entity -> entity.getDateAndTime() == null,
        (entity, builder) -> builder.translatable("some.identifier.in.language.properties",
        entity.getUser1().toString(),entity.getUser2().toString()));
        // if you have this in language.properties: some.identifier.in.language.properties=user1: %s, user2: %s
        // this will return a Response object that has all matched trades with the translatable: user1: %s, user2: %s

        // Remove
        tradeRepository.remove(getSomeTrade);

        // Get Id
        tradeRepository.getId(getSomeTrade);
         */
    }

    public Response createTrade(long user1, long user2, long item1, long item2, Boolean isPermanent,
                             Date dateAndTime, String location) {
        // Get User from Repository and check if the items are in their inventory
        // if (userRepository.ifExists(user1) && userRepository.ifExists(user2)) {
            //PersonalUser trader1 = userRepository.get(user1);
            //PersonalUser trader2 = userRepository.get(user2);
            // TODO: move conditions to controller? or fix here
            //if ((item1.equals(null) || trader1.getInventory().contains(item1)) && (item2.equals(null) ||
              //      trader2.getInventory().contains(item2))) {
                Trade newTrade = new Trade(user1, user2, item1, item2, isPermanent,
                        dateAndTime, location);
                tradeRepository.add(newTrade);
                return tradeRepresentation("submit.trade.represent", newTrade);
            //}
        //}
        //return null;
    }

    public Response editDateAndTime(int tradeID, int editingUser, Date dateAndTime) {
        // Get trade from Repository
        if (tradeRepository.ifExists(tradeID)) {
            Trade currTrade = tradeRepository.get(tradeID);
            if (currTrade.getUser1Edits() == editLimit && currTrade.getUser2Edits() == editLimit) {
                tradeRepository.remove(currTrade);
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
            return tradeRepresentation("submit.trade.represent", currTrade);
        }
        return new Response.Builder(false).translatable("failed.edit.trade").build();
    }

    public Response editLocation(int tradeID, int editingUser, String location) {
        // Get Trade from Repository
        if (tradeRepository.ifExists(tradeID)) {
            Trade currTrade = tradeRepository.get(tradeID);
            if (currTrade.getUser1Edits() == editLimit && currTrade.getUser2Edits() == editLimit) {
                tradeRepository.remove(currTrade);
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
            return tradeRepresentation("submit.trade.represent", currTrade);
        }
        return new Response.Builder(false).translatable("failed.edit.trade").build();
    }

    public void confirmTrade(int tradeID, int editingUser) {
        // Get Trade from Repository
        if (tradeRepository.ifExists(tradeID)) {
            Trade currTrade = tradeRepository.get(tradeID);

            // Confirm specific user
            if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms()) {
                currTrade.confirmUser1();
            } else if (currTrade.getUser2() == editingUser && !currTrade.getUser1Confirms()) {
                currTrade.confirmUser2();
            }

            // Open trade if both users confirm
            if (currTrade.getUser1Confirms() && currTrade.getUser2Confirms()) {
                currTrade.openTrade();
                currTrade.unconfirmUser1();
                currTrade.unconfirmUser2();
                long oldMeeting = currTrade.getPrevMeeting();
                if (tradeRepository.ifExists(oldMeeting)) {
                    Trade oldTrade = tradeRepository.get(oldMeeting);
                    oldTrade.closeTrade();
                }
            }
        }
    }

    public void confirmTradeComplete(int tradeID, int editingUser) {
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
                }
            }
        }
    }

    // TODO: Add to other people's inventory... Weird system -- they can trade other people's items
    private void makeTrades(PersonalUser currUser, PersonalUser otherUser, Trade currTrade) {
        if (currTrade.getIsPermanent()) {
            if (currTrade.getItem1() == null && currTrade.getItem2() != null) {
                currUser.setBorrowCount(currUser.getBorrowCount() + 1);
                currUser.removeFromWishList(currTrade.getItem2());
                // currUser.addToInventory(currTrade.getItem2());
                otherUser.setLendCount(currUser.getLendCount() + 1);
                otherUser.removeFromInventory(currTrade.getItem2());
            } else if (currTrade.getItem2() == null && currTrade.getItem1() != null) {
                otherUser.setBorrowCount(currUser.getBorrowCount() + 1);
                otherUser.removeFromWishList(currTrade.getItem1());
                // otherUser.addToInventory(currTrade.getItem2());
                currUser.setLendCount(currUser.getLendCount() + 1);
                currUser.removeFromInventory(currTrade.getItem1());
            } else {
                currUser.setBorrowCount(currUser.getBorrowCount() + 1);
                currUser.setLendCount(currUser.getLendCount() + 1);
                currUser.removeFromWishList(currTrade.getItem2());
                currUser.removeFromInventory(currTrade.getItem1());
                // currUser.addToInventory(currTrade.getItem2());
                otherUser.setLendCount(currUser.getLendCount() + 1);
                otherUser.setBorrowCount(currUser.getBorrowCount() + 1);
                otherUser.removeFromWishList(currTrade.getItem1());
                otherUser.removeFromInventory(currTrade.getItem2());
                // otherUser.addToInventory(currTrade.getItem1());
            }
            currTrade.closeTrade();
        } else {
            Date newDateAndTime = currTrade.getDateAndTime();
            // newDateAndTime.set(Calendar.MONTH, timeLimit); // TODO: set new dates and new meetings
            //Trade secondMeeting = createTrade(currTrade.getUser1(), currTrade.getUser1(),
            //        currTrade.getItem2(), currTrade.getItem1(), true, newDateAndTime,
            //        currTrade.getLocation());
            //secondMeeting.setPrevMeeting((long) tradeRepository.getId(secondMeeting));
        }
    }

    private Response tradeRepresentation(String translatable, Trade trade) {
        return new Response.Builder(true).
                translatable(translatable, trade.getUid(), trade.getUser1(), trade.getUser2(), trade.getIsPermanent(),
                        trade.getDateAndTime(), trade.getLocation())
                .build();
    }
}

