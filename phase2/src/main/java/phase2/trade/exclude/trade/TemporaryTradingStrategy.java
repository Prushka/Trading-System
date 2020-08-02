package main.java.phase2.trade.exclude.trade;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This strategy describes how temporary trades need to schedule a second meeting to trade back items
 * @author Grace Leung
 */
class TemporaryTradingStrategy implements Tradable{

    Trade currTrade;

    TemporaryTradingStrategy(){}

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
                currTrade.openTrade();
                currTrade.unconfirmAll();
            } else if (!currTrade.getIsClosed() && currTrade.getAllConfirmed()){
                makeTrades();
                scheduleTradeBack();
            }
        }
    }

    // Adjusts transaction, borrow, and lend counts
    private void makeTrades() {
        int index = 0;
        while (index < currTrade.getAllUsers().size()){
            // Get a user and their respective desired items list
            PersonalUser user = userRepository.get(currTrade.getAllUsers().get(index));
            List<Integer> items = currTrade.getAllItems().get(index);
            user.addRecentTrades(currTrade.getUid());
            user.setNumTransactions(user.getNumTransactions() + 1);

            for (int item: items){
                Item currItem = itemRepository.get(item);
                user.removeFromWishList(item);
                user.setBorrowCount(user.getBorrowCount() + 1);
                currItem.setIsAvailable(false);
                PersonalUser other = userRepository.get(item.getOwner());
                other.setLendCount(other.getLendCount() + 1);
            }
        }
    }

    // Schedules a second trade meeting
    private void scheduleTradeBack() {
        LocalDateTime newDateAndTime = currTrade.getDateAndTime().plusMonths(timeLimit);
        createTrade(currTrade.getAllUsers(), currTrade.getAllItems(), newDateAndTime,
                currTrade.getLocation(), currTrade.getUid());
    }


}
