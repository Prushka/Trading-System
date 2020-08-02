package main.java.phase2.trade.exclude.trade;

import java.util.List;

public class PermanentTradingStrategy implements Tradable{

    public PermanentTradingStrategy(){}

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


    @Override
    void openTrade(int tradeID){
        // Get trade from repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Check that all users confirm
        if (currTrade.getAllConfirmed()){
            currTrade.openTrade();
            currTrade.unconfirmAll();
        }

        // Close first meeting if this is a second meeting to trade back
        if (!currTrade.getPrevMeeting().equals(null) && tradeRepository.ifExists(currTrade.getPrevMeeting())) {
            Trade oldTrade = tradeRepository.get(currTrade.getPrevMeeting());
            oldTrade.closeTrade();
        }
    }

    @Override
    void completeTrade(int tradeID) {
        // Get trade from repository
        Trade currTrade = tradeRepository.get(tradeID);

        if (currTrade.getAllConfirmed()){
            makeTrades(currTrade);
            currTrade.closeTrade();
        }
    }

    // Makes a one-way or two-way trade
    private void makeTrades(Trade currTrade) {
        int index = 0;
        while (index < currTrade.getAllUsers().size()){

            // Get a user and their respective desired items list
            PersonalUser user = userRepository.get(currTrade.getAllUsers().get(index));
            List<Integer> items = currTrade.getAllItems().get(index);

            // Update that they traded
            if (currTrade.getPrevMeeting().equals(null)){
                user.addRecentTrades(currTrade.getUid());
                user.setNumTransactions(user.getNumTransactions() + 1);
            }

            for (int item: items){
                Item currItem = itemRepository.get(item);
                user.removeFromWishList(item);
                currItem.setOwnerUsername(user.getUid());
                if (currTrade.getPrevMeeting().equals(null)) {
                    user.setBorrowCount(user.getBorrowCount() + 1);
                    PersonalUser other = userRepository.get(item.getOwner());
                    other.setLendCount(other.getLendCount() + 1);
                }
            }
        }
    }
}
