package main.java.phase2.trade.exclude.trade;

import java.time.LocalDateTime;

/**
 * Where trade dates and locations are edited
 * @author Grace Leung
 */
class TradeEditor {

    Integer editLimit;

    TradeEditor(int editLimit){
        this.editLimit = editLimit;
    }

    /**
     Edit location of a trade and cancels the trade if the users edited too much
     * @param tradeID The trade ID of the trade to be edited
     * @param editingUser The user ID of who wishes to edit this trade
     * @param dateAndTime The new date and time when this trade will take place
     */
    void editDateAndTime(int tradeID, int editingUser, LocalDateTime dateAndTime) {
        // Get trade from Database
        Trade currTrade = tradeRepository.get(tradeID);

        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        if ((currTrade.getAllUsers().contains(editingUser)) && currTrade.getUserEdits(editingUser) == editLimit) {
            cancelTrades(currTrade);
        } else if (currTrade.getAllUsers().contains(editingUser) && !currTrade.getUserConfirms(editingUser) && currTrade.getUserEdits(editingUser) < editLimit) {
            currTrade.setDateAndTime(dateAndTime);
            currTrade.increaseUserEdits(editingUser);
            currTrade.confirmUser(editingUser);
            for (int i: currTrade.getAllUsers()){
                if (i != editingUser) {
                    currTrade.unconfirmUser(i);
                }
            }
        }
    }

    /**
     Edit location of a trade and cancels the trade if the users edited too much
     * @param tradeID The trade ID of the trade to be edited
     * @param editingUser The user ID of who wishes to edit this trade
     * @param location The new location of where this trade will take place
     */
    void editLocation(int tradeID, int editingUser, String location) {
        // Get trade from Repository
        Trade currTrade = tradeRepository.get(tradeID);

        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        if ((currTrade.getAllUsers().contains(editingUser)) && currTrade.getUserEdits(editingUser) == editLimit) {
            cancelTrades(currTrade);
        } else if (currTrade.getAllUsers().contains(editingUser) && !currTrade.getUserConfirms(editingUser) && currTrade.getUserEdits(editingUser) < editLimit) {
            currTrade.setLocation(location);
            currTrade.increaseUserEdits(editingUser);
            currTrade.confirmUser(editingUser);
            for (int i: currTrade.getAllUsers()){
                if (i != editingUser) {
                    currTrade.unconfirmUser(i);
                }
            }
        }
    }

    // Removes a trade from user's trade list and the trade repository
    private void cancelTrades(Trade currTrade) {
        for (int i: currTrade.getAllUsers()){
            PersonalUser user = userRepository.get(i);
            user.removeFromTrade(currTrade.getUid());
        }
        tradeRepository.remove(currTrade);
    }
}
