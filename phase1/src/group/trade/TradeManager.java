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
        this.tradeRepository = tradeRepository;
        this.userRepository = userRepository;

        /*
        Repository<Trade> tradeRepositorySerialization = new SerializableRepository<>("data/trade.ser");

        // PersonalUser::new refers to this constructor:
        public User(List<String> record){super(record);}

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
        boolean ifSomeTradeExists3 = tradeRepository.ifExists(new Trade()); Implement the equals() and hashCode() in Trade to use this one

        // TODO: Map Response
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

    // Come up with solution to the casting problem
    public Trade createTrade(long user1, long user2, Item item1, Item item2, boolean isPermanent,
                             Calendar dateAndTime, String location) {
        // Get User from Repository and check if the items are in their inventory
        if (userRepository.ifExists((int) user1) && userRepository.ifExists((int) user2)) {
            User trader1 = userRepository.get((int) user1);
            User trader2 = userRepository.get((int) user2);
            if ((item1 == null || ((PersonalUser) trader1).getInventory().contains(item1)) && (item2 == null ||
                    ((PersonalUser) trader2).getInventory().contains(item2))) {
                Trade newTrade = new Trade(numOfTrades, user1, user2, item1, item2, isPermanent, dateAndTime,
                        location);
                numOfTrades++;
                tradeRepository.add(newTrade);
                return newTrade;
            }
        }
        return null;
    }

    public void editDateAndTime(int tradeID, int editingUser, Calendar dateAndTime) {
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
        }
    }

    public void editLocation(int tradeID, int editingUser, String location) {
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
        }
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
                if (tradeRepository.ifExists((int) oldMeeting)) {
                    Trade oldTrade = tradeRepository.get((int) oldMeeting);
                    oldTrade.closeTrade();
                }
            }
        }
    }

    // More casting problems & shorten code
    public void confirmTradeComplete(int tradeID, int editingUser) {
        if (userRepository.ifExists(editingUser) && tradeRepository.ifExists(tradeID)) {
            PersonalUser currUser = (PersonalUser) userRepository.get(editingUser);
            Trade currTrade = tradeRepository.get(tradeID);
            // Confirm specific user
            if (currTrade.getUser1() == editingUser && !currTrade.getUser1Confirms()) {
                currTrade.confirmUser1();
                PersonalUser otherUser = (PersonalUser) userRepository.get((int) currTrade.getUser2());
                if (currTrade.getUser1Confirms() && currTrade.getUser2Confirms()) {
                    makeTrades(currUser, otherUser, currTrade);
                }
            } else if (currTrade.getUser2() == editingUser && !currTrade.getUser1Confirms()) {
                currTrade.confirmUser2();
                PersonalUser otherUser = (PersonalUser) userRepository.get((int) currTrade.getUser1());
                if (currTrade.getUser1Confirms() && currTrade.getUser2Confirms()) {
                    makeTrades(currUser, otherUser, currTrade);
                }
            }
        }
    }

    // TODO: Remove from wishlist and inventory
    private void makeTrades(PersonalUser currUser, PersonalUser otherUser, Trade currTrade) {
        if (currTrade.getIsPermanent()) {
            if (currTrade.getItem1() == null && currTrade.getItem2() != null) {
                currUser.setBorrowCount(currUser.getBorrowCount() + 1);
                otherUser.setLendCount(currUser.getLendCount() + 1);
            } else if (currTrade.getItem2() == null && currTrade.getItem1() != null) {
                otherUser.setBorrowCount(currUser.getBorrowCount() + 1);
                currUser.setLendCount(currUser.getLendCount() + 1);
            } else {
                currUser.setBorrowCount(currUser.getBorrowCount() + 1);
                currUser.setLendCount(currUser.getLendCount() + 1);
                otherUser.setLendCount(currUser.getLendCount() + 1);
                otherUser.setBorrowCount(currUser.getBorrowCount() + 1);
            }
            currTrade.closeTrade();
        } else {
            Calendar newDateAndTime = currTrade.getDateAndTime();
            newDateAndTime.set(Calendar.MONTH, borrowTimeLimit); // TODO: need to change, mappable base
            Trade secondMeeting = createTrade(currTrade.getUser1(), currTrade.getUser1(),
                    currTrade.getItem1(), currTrade.getItem2(), true, newDateAndTime,
                    currTrade.getLocation());
            currTrade.setPrevMeeting((long) tradeRepository.getId(secondMeeting));
        }
    }
}


