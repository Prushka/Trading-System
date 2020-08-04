package phase2.trade.trade;

import phase2.trade.user.Address;
import phase2.trade.user.User;

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
     * @param editingUser The user ID of who wishes to edit this trade
     * @param dateAndTime The new date and time when this trade will take place
     */
    void editDateAndTime(Trade currTrade, int editingUser, LocalDateTime dateAndTime) {
        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        if ((currTrade.getAllUsers().contains(editingUser)) && currTrade.getUserEdits(editingUser) == editLimit) {
            cancelTrades(currTrade);
        } else if (currTrade.getAllUsers().contains(editingUser) && !currTrade.getUserConfirms(editingUser) && currTrade.getUserEdits(editingUser) < editLimit) {
            currTrade.setDateAndTime(dateAndTime);
            currTrade.increaseUserEdits(editingUser);
            currTrade.confirmUser(editingUser);
            for (User i: currTrade.getAllUsers()){
                if (i.getUid() != editingUser) {
                    // currTrade.unconfirmUser(i);
                }
            }
        }
    }

    /**
     Edit location of a trade and cancels the trade if the users edited too much
     * @param editingUser The user ID of who wishes to edit this trade
     * @param location The new location of where this trade will take place
     */
    void editLocation(Trade currTrade, int editingUser, Address location) {
        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        if ((currTrade.getAllUsers().contains(editingUser)) && currTrade.getUserEdits(editingUser) == editLimit) {
            cancelTrades(currTrade);
        } else if (currTrade.getAllUsers().contains(editingUser) && !currTrade.getUserConfirms(editingUser) && currTrade.getUserEdits(editingUser) < editLimit) {
            currTrade.setLocation(location);
            currTrade.increaseUserEdits(editingUser);
            currTrade.confirmUser(editingUser);
            for (User i: currTrade.getAllUsers()){
                if (i.getUid() != editingUser) {
                    // currTrade.unconfirmUser(i.getUid());
                }
            }
        }
    }

    // Removes a trade from user's trade list and the trade repository
    private void cancelTrades(Trade currTrade) {
        // for (int i: currTrade.getAllUsers()){
            // PersonalUser user = userRepository.get(i);
            // user.removeFromTrade(currTrade.getUid());
        // }
        // tradeRepository.remove(currTrade);
    }
}
