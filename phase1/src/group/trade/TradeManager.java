package group.trade;

import group.config.property.TradeProperties;
import group.item.Item;
import group.menu.data.Response;
import group.repository.CSVRepository;
import group.repository.Repository;
import group.user.User;
import group.user.PersonalUser;
import java.sql.Timestamp;
import java.util.Iterator;

public class TradeManager{
    private int numOfTrades;
    private int editLimit;
    private int borrowTimeLimit; // the number of months until a user has to reverse the temporary trade
    private Repository<Trade> tradeRepository;
    private Repository<PersonalUser> userRepository;

    private Trade curr_trade;

    public TradeManager(TradeProperties tradeProperties){
        // Default Values for trade information stored in tradeProperties:
        tradeProperties.set("editLimit","3");
        tradeProperties.set("borrowTimeLimit", "1");
        editLimit = Integer.parseInt(tradeProperties.get("editLimit"));
        borrowTimeLimit = Integer.parseInt(tradeProperties.get("editLimit"));

        // Repository -- should be taken as a parameter from TradeSystem? Controller
        // Repository<Trade> tradeRepositorySerialization = new SerializableRepository<>("data/trade.ser");
        Repository<Trade> tradeRepository = new CSVRepository<>("data/trade.csv", Trade::new);
        Repository<Trade> userRepository = new CSVRepository<>("data/trade.csv", PersonalUser::new);

        // Get from Repository -- use for only one record
        Trade getSomeTrade = tradeRepository.getFirst(entity -> entity.getItem1() == null);
        Iterator<Trade> getAnIterator = tradeRepository.iterator(entity -> entity.getItem1() == null);
        Iterator<Trade> getAnIterator2 = tradeRepository.iterator(entity -> {
            if(entity.getUser1Confirms()){

            }else if(entity.getUser2Confirms()){

            }
            return false;
        });
        Trade getSomeTradeByUID = tradeRepository.get(10);

        // If entity exists in Repository
        boolean ifSomeTradeExists = tradeRepository.ifExists(entity -> entity.getDateAndTime()==null);
        boolean ifSomeTradeExists2 = tradeRepository.ifExists(4);
        // boolean ifSomeTradeExists3 = tradeRepository.ifExists(new Trade()); Implement the equals() and hashCode() in Trade to use this one

        //Map Response
        Response response = tradeRepository.filterResponse(entity -> entity.getDateAndTime() == null,
                (entity, builder) -> builder.translatable("some.identifier.in.language.properties",entity.getUser1().toString(),entity.getUser2().toString()));
        // if you have this in language.properties: some.identifier.in.language.properties=user1: %s, user2: %s
        // this will return a Response object that has all matched trades with the translatable: user1: %s, user2: %s
    }

    public void createTrade(int user1, int user2, Item item1, Item item2, boolean isPermanent,
                            Timestamp dateAndTime, String location){
        // Check if the items are in the user's inventory
        if (userRepository.ifExists(user1) && userRepository.ifExists(user2)){
            // Get Trade from Repository
            PersonalUser trader1 = userRepository.get(user1);
            PersonalUser trader2 = userRepository.get(user2);
            if ((item1 == null || trader1.getInventory().contains(item1)) && (item2 == null ||
                    trader2.getInventory().contains(item2)) ) {
                Trade new_trade = new Trade(numOfTrades, user1, user2, item1, item2, isPermanent, dateAndTime, location);
                numOfTrades++;
                tradeRepository.add(new_trade);
            }
        }
    }

    public void editDateAndTime(int tradeID, Long editing_user, Timestamp dateAndTime){
        // If entity exists in Repository
        if (tradeRepository.ifExists(tradeID)){
            // Get Trade from Repository
            Trade curr_trade = tradeRepository.get(tradeID);
            if (curr_trade.getUser1().toString().equals(editing_user.toString()) && !curr_trade.getUser1Confirms() &&
                    curr_trade.getUser1Edits() < editLimit){
                curr_trade.setDateAndTime(dateAndTime);
                curr_trade.increaseUser1Edits();
                curr_trade.confirmUser1();
                curr_trade.unconfirmUser2();
            } else if (curr_trade.getUser2().toString().equals(editing_user.toString()) && !curr_trade.getUser2Confirms() &&
                    curr_trade.getUser2Edits() < editLimit){
                curr_trade.setDateAndTime(dateAndTime);
                curr_trade.increaseUser2Edits();
                curr_trade.unconfirmUser1();
                curr_trade.confirmUser2();
            } else if (curr_trade.getUser1Edits() == editLimit && curr_trade.getUser2Edits() == editLimit) {
                // remove from repository
            }
        }
    }

    public String editLocation(int tradeID, PersonalUser editing_user, String location){
        // If entity exists in Repository
        if (tradeRepository.ifExists(tradeID)) {
            // Get Trade from Repository
            Trade curr_trade = tradeRepository.get(tradeID);
            if (curr_trade.getUser1().toString().equals(editing_user.toString()) && !curr_trade.getUser1Confirms() &&
                    curr_trade.getUser1Edits() < editLimit) {
                curr_trade.setLocation(location);
                curr_trade.increaseUser1Edits();
                curr_trade.confirmUser1();
                curr_trade.unconfirmUser2();
                return "Successful edit!";
            } else if (curr_trade.getUser2().toString().equals(editing_user.toString()) && !curr_trade.getUser2Confirms() &&
                    curr_trade.getUser2Edits() < editLimit) {
                curr_trade.setLocation(location);
                curr_trade.increaseUser2Edits();
                curr_trade.unconfirmUser1();
                curr_trade.confirmUser2();
                return "Successful edit!";
            } else if (curr_trade.getUser1Edits() == editLimit && curr_trade.getUser2Edits() == editLimit) {
                // remove from repository...
                return "Too many edit attempts, this trade is cancelled.";
            } else {
                return editing_user.getName() + " cannot edit.";
            }
        }
    }

    public String confirmTrade(int tradeID, PersonalUser editing_user){
        // If entity exists in Repository
        if (tradeRepository.ifExists(tradeID)) {
            // Get Trade from Repository
            Trade curr_trade = tradeRepository.get(tradeID);
            if (curr_trade.getUser1().toString().equals(editing_user.toString()) && !curr_trade.getUser1Confirms()) {
                curr_trade.confirmUser1();
            } else if (curr_trade.getUser2().toString().equals(editing_user.toString()) && !curr_trade.getUser1Confirms()) {
                curr_trade.confirmUser2();
            } else if (!(curr_trade.getUser1().toString().equals(editing_user.toString()) &&
                    curr_trade.getUser2().toString().equals(editing_user.toString()))) {
                return editing_user.getName() + " does not belong to this trade.";
            }

            if (curr_trade.getUser1Confirms() && curr_trade.getUser2Confirms()) {
                curr_trade.openTrade();
                curr_trade.unconfirmUser1();
                curr_trade.unconfirmUser2();
                return "This trade is now confirmed.";
            } else {
                return "Awaiting other confirmation";
            }
        }
    }

    public String confirmTradeComplete(int tradeID, PersonalUser editing_user){
        // If entity exists in Repository
        if (tradeRepository.ifExists(tradeID)){
            // Get Trade from Repository
            Trade curr_trade = tradeRepository.get(tradeID);
        if (curr_trade.getUser1().toString().equals(editing_user.toString()) && !curr_trade.getUser1Confirms()){
            curr_trade.confirmUser1();
        } else if (curr_trade.getUser2().toString().equals(editing_user.toString()) && !curr_trade.getUser1Confirms()){
            curr_trade.confirmUser2();
        } else if (!(curr_trade.getUser1().toString().equals(editing_user.toString()) &&
                curr_trade.getUser2().toString().equals(editing_user.toString()))){
            return editing_user.getName() + " does not belong to this trade.";
        }


        // Remove from wishlist and inventory
        if (curr_trade.getUser1Confirms() && curr_trade.getUser2Confirms()){
            if (curr_trade.getIsPermanent()) {
                if (curr_trade.getItem1() == null && curr_trade.getItem2() != null){
                    curr_trade.getUser1().setBorrowCount(curr_trade.getUser1().getBorrowCount() + 1);
                    curr_trade.getUser2().setLendCount(curr_trade.getUser2().getLendCount() + 1);
                } else if (curr_trade.getItem2() == null && curr_trade.getItem1() != null){
                    curr_trade.getUser2().setBorrowCount(curr_trade.getUser2().getBorrowCount() + 1);
                    curr_trade.getUser1().setLendCount(curr_trade.getUser1().getLendCount() + 1);
                } else {
                    curr_trade.getUser1().setBorrowCount(curr_trade.getUser1().getBorrowCount() + 1);
                    curr_trade.getUser1().setLendCount(curr_trade.getUser1().getLendCount() + 1);
                    curr_trade.getUser2().setLendCount(curr_trade.getUser2().getLendCount() + 1);
                    curr_trade.getUser2().setBorrowCount(curr_trade.getUser2().getBorrowCount() + 1);
                }
                curr_trade.closeTrade();
                return "This trade is closed.";
            } else {
                // how to keep this open while they confirm this next trade
                Timestamp new_DateAndTime = (Timestamp) curr_trade.getDateAndTime().clone();

                // change
                new_DateAndTime.setMonth(curr_trade.getDateAndTime().getMonth() + 1);
                createTrade(curr_trade.getUser1(), curr_trade.getUser2(), curr_trade.getItem1(), curr_trade.getItem2(),
                        curr_trade.getIsPermanent(), new_DateAndTime, curr_trade.getLocation());
                return "A second meeting is available to trade back items.";
            }
        } else {
            return "Awaiting other confirmation";
        }
        }

}

