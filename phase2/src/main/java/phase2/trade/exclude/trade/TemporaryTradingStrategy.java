package main.java.phase2.trade.exclude.trade;

import java.time.LocalDateTime;
import java.util.List;

public class TemporaryTradingStrategy implements Tradable{

    public TemporaryTradingStrategy(){}
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

        if (currTrade.getAllConfirmed()){
            currTrade.openTrade();
            currTrade.unconfirmAll();
        }
    }

    @Override
    void completeTrade(int tradeID) {
        // Get trade from repository
        Trade currTrade = tradeRepository.get(tradeID);

        // If all users confirm, make the trade
        if (currTrade.getAllConfirmed()){
            makeTrades(currTrade);
            scheduleTradeBack(currTrade);
        }
    }

    private void makeTrades(Trade currTrade) {
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
    private void scheduleTradeBack(Trade currTrade) {
        LocalDateTime newDateAndTime = currTrade.getDateAndTime().plusMonths(timeLimit);
        createTrade(currTrade.getAllUsers(), currTrade.getAllItems(), newDateAndTime,
                currTrade.getLocation(), currTrade.getUid());
    }


}
