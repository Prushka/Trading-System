package group;

import java.sql.Timestamp;

// Reduce repeat code
public class TradeManager{
    private int editLimit;
    private int timeLimit; // the number of months until a user has to reverse the temporary trade
    private int numOfTrades;
    // private CSVRepository storage;

    Trade curr_trade;

    public TradeManager(){
        // Default Values
        editLimit = 3;
        numOfTrades = 1;
        // storage = new CSVRepository("data/a.csv", Ticket::new);
    }

    public void setEditLimit(int editLimit) { this.editLimit = editLimit;}
    public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit;}

    public void createTrade(PersonalUser user1, PersonalUser user2, Item item1, Item item2, boolean isPermanent,
                            Timestamp dateAndTime, String location){
        // Check if the items are in the user's inventory
        if ((item1 == null || user1.getInventory().contains(item1)) && (item2 == null ||
                user2.getInventory().contains(item2)) ) {
            Trade new_trade = new Trade(numOfTrades, user1, user2, item1, item2, isPermanent, dateAndTime, location);
            numOfTrades++;
            // storage.add(new_trade);
        }
    }

    public String editDateAndTime(int tradeID, PersonalUser editing_user, Timestamp dateAndTime){
        if (curr_trade.getUser1().toString().equals(editing_user.toString()) && !curr_trade.getUser1Confirms() &&
                curr_trade.getUser1Edits() < editLimit){
            curr_trade.setDateAndTime(dateAndTime);
            curr_trade.increaseUser1Edits();
            curr_trade.confirmUser1();
            curr_trade.unconfirmUser2();
            return "Successful edit!";
        } else if (curr_trade.getUser2().toString().equals(editing_user.toString()) && !curr_trade.getUser2Confirms() &&
                curr_trade.getUser2Edits() < editLimit){
            curr_trade.setDateAndTime(dateAndTime);
            curr_trade.increaseUser2Edits();
            curr_trade.unconfirmUser1();
            curr_trade.confirmUser2();
            return "Successful edit!";
        } else if (curr_trade.getUser1Edits() == editLimit && curr_trade.getUser2Edits() == editLimit){
            // remove from repository...
            return "Too many edit attempts, this trade is cancelled.";
        } else {
            return editing_user.getName() + " cannot edit this trade!";
        }
    }

    public String editLocation(int tradeID, PersonalUser editing_user, String location){
        if (curr_trade.getUser1().toString().equals(editing_user.toString()) && !curr_trade.getUser1Confirms() &&
                curr_trade.getUser1Edits() < editLimit){
            curr_trade.setLocation(location);
            curr_trade.increaseUser1Edits();
            curr_trade.confirmUser1();
            curr_trade.unconfirmUser2();
            return "Successful edit!";
        } else if (curr_trade.getUser2().toString().equals(editing_user.toString()) && !curr_trade.getUser2Confirms() &&
                curr_trade.getUser2Edits() < editLimit){
            curr_trade.setLocation(location);
            curr_trade.increaseUser2Edits();
            curr_trade.unconfirmUser1();
            curr_trade.confirmUser2();
            return "Successful edit!";
        } else if (curr_trade.getUser1Edits() == editLimit && curr_trade.getUser2Edits() == editLimit){
            // remove from repository...
            return "Too many edit attempts, this trade is cancelled.";
        } else {
            return editing_user.getName() + " cannot edit.";
        }
    }

    public String confirmTrade(int tradeID, PersonalUser editing_user){
        if (curr_trade.getUser1().toString().equals(editing_user.toString()) && !curr_trade.getUser1Confirms()){
            curr_trade.confirmUser1();
        } else if (curr_trade.getUser2().toString().equals(editing_user.toString()) && !curr_trade.getUser1Confirms()){
            curr_trade.confirmUser2();
        } else if (!(curr_trade.getUser1().toString().equals(editing_user.toString()) &&
                curr_trade.getUser2().toString().equals(editing_user.toString()))){
            return editing_user.getName() + " does not belong to this trade.";
        }

        if (curr_trade.getUser1Confirms() && curr_trade.getUser2Confirms()){
            curr_trade.openTrade();
            curr_trade.unconfirmUser1();
            curr_trade.unconfirmUser2();
            return "This trade is now confirmed.";
        } else {
            return "Awaiting other confirmation";
        }
    }

    public String confirmTradeComplete(int tradeID, PersonalUser editing_user){
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

