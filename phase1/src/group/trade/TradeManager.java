package group.trade;

import group.config.property.TradeProperties;
import group.item.Item;
import group.repository.Repository;
import group.user.PersonalUser;
import group.user.User;

import java.util.Calendar;
import java.util.Iterator;

public class TradeManager {
    private int numOfTrades;
    private final int editLimit;
    private final int borrowTimeLimit; // the number of months until a user has to reverse the temporary trade
    private Repository<Trade> tradeRepository;
    private Repository<User> userRepository;

    public TradeManager(Repository<Trade> tradeRepository, Repository<User> userRepository, TradeProperties tradeProperties) {
        // Default Values for trade information stored in tradeProperties:
        tradeProperties.set("editLimit", "3");
        tradeProperties.set("borrowTimeLimit", "1");
        editLimit = Integer.parseInt(tradeProperties.get("editLimit"));
        borrowTimeLimit = Integer.parseInt(tradeProperties.get("editLimit"));

        // Repository -- should be taken as a parameter from TradeSystem? Controller
        // Repository<Trade> tradeRepositorySerialization = new SerializableRepository<>("data/trade.ser");

        // PersonalUser::new refers to this constructor:
        // public User(List<String> record){super(record);}

        // if this constructor does not exist in your entity, it will throw an error

        // flow:
        // CSVRepository takes in this constructor reference as a field. And when reading from csv file,
        // this constructor will be called to create corresponding objects.
        // The constructor is a standard list of strings since that's how csv columns work

        // CSVRepository is only dependent on EntityMappable not MappableBase
        // So it is optional to extend MappableBase (though I would recommend you do that).
        // MappableBase is just a reflection implementation of EntityMappable
        // If you don't extend MappableBase just make sure you implement EntityMappable and all methods needed including
        // the constructor that's mentioned above,
        // this will get rid of all restrictions that are basically caused by reflection. (You will need to put your
        // fields into String manually and put them back in the constructor)

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
        // boolean ifSomeTradeExists3 = tradeRepository.ifExists(new Trade()); Implement the equals() and hashCode() in Trade to use this one

        // TODO: Map Response
        // Response response = tradeRepository.filterResponse(entity -> entity.getDateAndTime() == null,
        //        (entity, builder) -> builder.translatable("some.identifier.in.language.properties",
        //        entity.getUser1().toString(),entity.getUser2().toString()));
        // if you have this in language.properties: some.identifier.in.language.properties=user1: %s, user2: %s
        // this will return a Response object that has all matched trades with the translatable: user1: %s, user2: %s

        // Remove
        tradeRepository.remove(getSomeTrade);

        // Get Id
        tradeRepository.getId(getSomeTrade);
    }

    // Come up with solution to the casting problem
    public Trade createTrade(long user1, long user2, Item item1, Item item2, boolean isPermanent,
                             Calendar dateAndTime, String location) {
        // Get User from Repository and check if the items are in their inventory
        if (userRepository.ifExists((int) user1) && userRepository.ifExists((int) user2)) {
            User trader1 = userRepository.get((int) user1);
            User trader2 = userRepository.get((int) user2);
            if ((item1 == null || ((PersonalUser) trader1).getInventory().contains(item1)) && (item2 == null ||
                    ((PersonalUser) trader2).getInventory().contains(item2))) {
                Trade new_trade = new Trade(numOfTrades, user1, user2, item1, item2, isPermanent, dateAndTime,
                        location);
                numOfTrades++;
                tradeRepository.add(new_trade);
                return new_trade;
            }
        }
        return null;
    }

    public void editDateAndTime(int tradeID, int editing_user, Calendar dateAndTime) {
        // Get trade from Repository
        if (tradeRepository.ifExists(tradeID)) {
            Trade curr_trade = tradeRepository.get(tradeID);
            if (curr_trade.getUser1Edits() == editLimit && curr_trade.getUser2Edits() == editLimit) {
                tradeRepository.remove(curr_trade);
            } else if (curr_trade.getUser1() == editing_user && !curr_trade.getUser1Confirms() &&
                    curr_trade.getUser1Edits() < editLimit) {
                curr_trade.setDateAndTime(dateAndTime);
                curr_trade.increaseUser1Edits();
                curr_trade.confirmUser1();
                curr_trade.unconfirmUser2();
            } else if (curr_trade.getUser2() == editing_user && !curr_trade.getUser2Confirms() &&
                    curr_trade.getUser2Edits() < editLimit) {
                curr_trade.setDateAndTime(dateAndTime);
                curr_trade.increaseUser2Edits();
                curr_trade.unconfirmUser1();
                curr_trade.confirmUser2();
            }
        }
    }

    public void editLocation(int tradeID, int editing_user, String location) {
        // Get Trade from Repository
        if (tradeRepository.ifExists(tradeID)) {
            Trade curr_trade = tradeRepository.get(tradeID);
            if (curr_trade.getUser1Edits() == editLimit && curr_trade.getUser2Edits() == editLimit) {
                tradeRepository.remove(curr_trade);
            } else if (curr_trade.getUser1() == editing_user && !curr_trade.getUser1Confirms() &&
                    curr_trade.getUser1Edits() < editLimit) {
                curr_trade.setLocation(location);
                curr_trade.increaseUser1Edits();
                curr_trade.confirmUser1();
                curr_trade.unconfirmUser2();
            } else if (curr_trade.getUser2() == editing_user && !curr_trade.getUser2Confirms() &&
                    curr_trade.getUser2Edits() < editLimit) {
                curr_trade.setLocation(location);
                curr_trade.increaseUser2Edits();
                curr_trade.unconfirmUser1();
                curr_trade.confirmUser2();
            }
        }
    }

    public void confirmTrade(int tradeID, int editing_user) {
        // Get Trade from Repository
        if (tradeRepository.ifExists(tradeID)) {
            Trade curr_trade = tradeRepository.get(tradeID);

            // Confirm specific user
            if (curr_trade.getUser1() == editing_user && !curr_trade.getUser1Confirms()) {
                curr_trade.confirmUser1();
            } else if (curr_trade.getUser2() == editing_user && !curr_trade.getUser1Confirms()) {
                curr_trade.confirmUser2();
            }

            // Open trade if both users confirm
            if (curr_trade.getUser1Confirms() && curr_trade.getUser2Confirms()) {
                curr_trade.openTrade();
                curr_trade.unconfirmUser1();
                curr_trade.unconfirmUser2();
                long old_meeting = curr_trade.getPrevMeeting();
                if (tradeRepository.ifExists((int) old_meeting)) {
                    Trade old_trade = tradeRepository.get((int) old_meeting);
                    old_trade.closeTrade();
                }
            }
        }
    }

    // More casting problems & shorten code
    public void confirmTradeComplete(int tradeID, int editing_user) {
        if (userRepository.ifExists(editing_user) && tradeRepository.ifExists(tradeID)) {
            PersonalUser curr_user = (PersonalUser) userRepository.get(editing_user);
            Trade curr_trade = tradeRepository.get(tradeID);
            // Confirm specific user
            if (curr_trade.getUser1() == editing_user && !curr_trade.getUser1Confirms()) {
                curr_trade.confirmUser1();
                PersonalUser other_user = (PersonalUser) userRepository.get((int) curr_trade.getUser2());
                if (curr_trade.getUser1Confirms() && curr_trade.getUser2Confirms()) {
                    makeTrades(curr_user, other_user, curr_trade);
                }
            } else if (curr_trade.getUser2() == editing_user && !curr_trade.getUser1Confirms()) {
                curr_trade.confirmUser2();
                PersonalUser other_user = (PersonalUser) userRepository.get((int) curr_trade.getUser1());
                if (curr_trade.getUser1Confirms() && curr_trade.getUser2Confirms()) {
                    makeTrades(curr_user, other_user, curr_trade);
                }
            }
        }
    }

    // TODO: Remove from wishlist and inventory
    private void makeTrades(PersonalUser curr_user, PersonalUser other_user, Trade curr_trade) {
        if (curr_trade.getIsPermanent()) {
            if (curr_trade.getItem1() == null && curr_trade.getItem2() != null) {
                curr_user.setBorrowCount(curr_user.getBorrowCount() + 1);
                other_user.setLendCount(curr_user.getLendCount() + 1);
            } else if (curr_trade.getItem2() == null && curr_trade.getItem1() != null) {
                other_user.setBorrowCount(curr_user.getBorrowCount() + 1);
                curr_user.setLendCount(curr_user.getLendCount() + 1);
            } else {
                curr_user.setBorrowCount(curr_user.getBorrowCount() + 1);
                curr_user.setLendCount(curr_user.getLendCount() + 1);
                other_user.setLendCount(curr_user.getLendCount() + 1);
                other_user.setBorrowCount(curr_user.getBorrowCount() + 1);
            }
            curr_trade.closeTrade();
        } else {
            Calendar new_DateAndTime = curr_trade.getDateAndTime();
            new_DateAndTime.set(Calendar.MONTH, borrowTimeLimit); // TODO: need to change
            Trade second_meeting = createTrade(curr_trade.getUser1(), curr_trade.getUser1(),
                    curr_trade.getItem1(), curr_trade.getItem2(), true, new_DateAndTime,
                    curr_trade.getLocation());
            curr_trade.setPrevMeeting((long) tradeRepository.getId(second_meeting));
        }
    }
}


