package phase2.trade.trade;

import phase2.trade.address.Address;
import phase2.trade.user.User;

import java.time.LocalDateTime;

/**
 * Where trade dates and locations are edited
 * @author Grace Leung
 */
public class TradeEditor {

    Integer editLimit;

    public TradeEditor(int editLimit){
        this.editLimit = editLimit;
    }

    public Trade edit(Trade currTrade, User editingUser, String... args) {
        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        for (UserOrderBundle user: currTrade.getOrder().getTraders()){
            if (user.getUser().equals(editingUser) && !user.getConfirmations() && user.getEdits() == editLimit){
                cancelOrder(currTrade);
            } else if (user.getUser().equals(editingUser) && !user.getConfirmations() && user.getEdits() < editLimit){
                if (args.length == 5){
                    LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                            Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                    currTrade.getOrder().setDateAndTime(dateTime);
                } else if (args.length == 4){
                    Address location = new Address(args[0], args[1], args[2], args[3]);
                    ((MeetUpOrder)currTrade.getOrder()).setLocation(location);
                }
                user.setEdits(user.getEdits() + 1);
                user.setConfirmations(true);
            } else if (!user.getUser().equals(editingUser)){
                user.setConfirmations(false); // Need to alter so if they aren't in the trade at all, this doesn't happen
            }
        }

        return currTrade;
    }

    private void cancelOrder(Trade currTrade) {
        currTrade.setTradeState(TradeState.CANCELLED);
    }
}
