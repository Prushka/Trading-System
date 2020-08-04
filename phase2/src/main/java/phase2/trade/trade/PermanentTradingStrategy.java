package phase2.trade.trade;

import phase2.trade.database.TradeDAO;
import phase2.trade.item.Item;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.List;

/**
 * This strategy describes how permanent trades may be the first meeting for a temporary trade
 * and changing item ownership
 * @author Grace Leung
 */
class PermanentTradingStrategy implements Tradable{

    Trade currTrade;

    PermanentTradingStrategy(Trade currTrade){
        this.currTrade = currTrade;
    }

    /**
     * Confirm a trade will take place and opens the trade
     * @param editingUser The user ID of who wishes to confirm to this trade
     */
    @Override
    public void confirmTrade(int editingUser) {
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
        if (!currTrade.getPrevMeeting().equals(null)) {
            // Trade oldTrade = tradeDAO.findById(currTrade.getPrevMeeting());
            // oldTrade.closeTrade();
        }
    }

    // Adjusts transaction, borrow, lend counts, and item ownership
    private void makeTrades() {
        int index = 0;
        while (index < currTrade.getAllUsers().size()){

            // Get a user and their respective desired items list
            PersonalUser user = currTrade.getAllUsers().get(index);
            List<Item> items = currTrade.getAllItems().get(index);

            // Update that they traded
            if (currTrade.getPrevMeeting().equals(null)){
                user.setNumTransactions(user.getNumTransactions() + 1);
            }

            for (Item item: items){
                // user.removeFromWishList(item);
                // item.setOwner(user);
                if (currTrade.getPrevMeeting().equals(null)) {
                    user.setBorrowCount(user.getBorrowCount() + 1);
                    // User other = item.getOwner();
                    // other.setLendCount(other.getLendCount() + 1);
                }
            }
        }
    }
}
