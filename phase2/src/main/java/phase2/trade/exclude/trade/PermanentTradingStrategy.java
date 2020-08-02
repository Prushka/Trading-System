package main.java.phase2.trade.exclude.trade;

import java.util.List;

/**
 * This strategy describes how permanent trades may be the first meeting for a temporary trade
 * and changing item ownership
 * @author Grace Leung
 */
class PermanentTradingStrategy implements Tradable{

    Trade currTrade;

    PermanentTradingStrategy(){}

    /**
     * Confirm a trade will take place and opens the trade
     * @param tradeID The trade ID of the trade to be confirmed
     * @param editingUser The user ID of who wishes to confirm to this trade
     */
    @Override
    public void confirmTrade(int tradeID, int editingUser) {
        // Get Trade from Database
        Trade currTrade = tradeRepository.get(tradeID);

        // Confirm specific user and checks if all users are confirmed yet
        if (currTrade.getAllUsers().contains(editingUser) && !currTrade.getUserConfirms(editingUser)
                && currTrade.getIsClosed()) {
            currTrade.confirmUser(editingUser);
            if (currTrade.getIsClosed() && currTrade.getAllConfirmed()){
                openTrade();
            } else if (!currTrade.getIsClosed() && currTrade.getAllConfirmed()){
                makeTrades();
                currTrade.closeTrade();
            }
        }
    }

    // Opens this trade and closes previous meetings
    void openTrade(){
        // Check that all users confirm
            currTrade.openTrade();
            currTrade.unconfirmAll();

        // Close first meeting if this is a second meeting to trade back
        if (!currTrade.getPrevMeeting().equals(null) && tradeRepository.ifExists(currTrade.getPrevMeeting())) {
            Trade oldTrade = tradeRepository.get(currTrade.getPrevMeeting());
            oldTrade.closeTrade();
        }
    }

    // Adjusts transaction, borrow, lend counts, and item ownership
    private void makeTrades() {
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
