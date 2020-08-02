package main.java.phase2.trade.exclude.trade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PermanentTradeManager extends TradeManager{

    public PermanentTradeManager(){
        super();
    }

    // Opens a trade and closes the previous trade if applicable
    private void openTrade(int tradeID){
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

    // Completes a trade by making trades or scheduling second meeting
    private void completeTrade(int tradeID) {
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
