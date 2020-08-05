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

    Trade editDateAndTime(Trade currTrade, User editingUser, LocalDateTime dateAndTime) {
        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        for (UserOrderBundle user: currTrade.getOrder().getTraders()){
            if (user.getUser().equals(editingUser) && !user.getConfirmations() && user.getEdits() == editLimit){
                cancelOrder(currTrade);
            } else if (user.getUser().equals(editingUser) && !user.getConfirmations() && user.getEdits() < editLimit){
                currTrade.getOrder().setDateAndTime(dateAndTime);
                user.setEdits(user.getEdits() + 1);
                user.setConfirmations(true);
            } else if (!user.getUser().equals(editingUser)){
                user.setConfirmations(false); // Need to alter so if they aren't in the trade at all, this doesn't happen
            }
        }

        return currTrade;
    }

    Trade editLocation(Trade currTrade, User editingUser, Address location) {
        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        for (UserOrderBundle user: currTrade.getOrder().getTraders()){
            if (user.getUser().equals(editingUser) && !user.getConfirmations() && user.getEdits() == editLimit){
                cancelOrder(currTrade);
            } else if (user.getUser().equals(editingUser) && !user.getConfirmations() && user.getEdits() < editLimit){
                ((MeetUpOrder) currTrade.getOrder()).setLocation(location);
                user.setEdits(user.getEdits() + 1);
                user.setConfirmations(true);
            } else if (!user.getUser().equals(editingUser)){
                user.setConfirmations(false);
            }
        }
        return currTrade;
    }

    private void cancelOrder(Trade currTrade) {
        currTrade.setTradeState(TradeState.CANCELLED);
    }
}
