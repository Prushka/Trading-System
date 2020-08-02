package main.java.phase2.trade.exclude.trade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TemporaryTradeManager extends TradeManager {

    public TemporaryTradeManager(){
        super();
    }

    private void openTrade(int tradeID){
        // Get trade from repository
        Trade currTrade = tradeRepository.get(tradeID);

        if (currTrade.getAllConfirmed()){
            currTrade.openTrade();
            currTrade.unconfirmAll();
        }
    }

    // Completes a trade by making trades or scheduling second meeting
    private void completeTrade(int tradeID) {
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
