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

    Trade editDateAndTime(Trade currTrade, Long orderID, User editingUser, LocalDateTime dateAndTime) {
        Order currOrder = currTrade.getOrders().get(0); // Change later
        for (Order order: currTrade.getOrders()){
            if (order.getUid().equals(orderID)){
                currOrder = order;
            }
        }

        UserOrderBundle init = currOrder.getInitiator();
        UserOrderBundle targ = currOrder.getTarget();

        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        if (init.getUser().equals(editingUser)){
            if (!init.getConfirmations()){
                if (init.getEdits() == editLimit) {
                    cancelOrder(currOrder);
                } else {
                    currOrder.setDateAndTime(dateAndTime);
                    init.setEdits(init.getEdits() + 1);
                    init.setConfirmations(true);
                    targ.setConfirmations(false);
                }
            }
        } else if (targ.getUser().equals(editingUser)){
            if (!targ.getConfirmations()){
                if (targ.getEdits() == editLimit) {
                    cancelOrder(currOrder);
                } else {
                    currOrder.setDateAndTime(dateAndTime);
                    targ.setEdits(targ.getEdits() + 1);
                    targ.setConfirmations(true);
                    init.setConfirmations(false);
                }
            }
        }

        return currTrade;
    }

    Trade editLocation(Trade currTrade, Long orderID, User editingUser, Address location) {
        Order currOrder = currTrade.getOrders().get(0); // Change later
        for (Order order: currTrade.getOrders()){
            if (order.getUid().equals(orderID)){
                currOrder = order;
            }
        }

        UserOrderBundle init = currOrder.getInitiator();
        UserOrderBundle targ = currOrder.getTarget();

        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        if (init.getUser().equals(editingUser)){
            if (!init.getConfirmations()){
                if (init.getEdits() == editLimit) {
                    cancelOrder(currOrder);
                } else {
                    ((MeetUpOrder) currOrder).setLocation(location);
                    init.setEdits(init.getEdits() + 1);
                    init.setConfirmations(true);
                    targ.setConfirmations(false);
                }
            }
        } else if (targ.getUser().equals(editingUser)){
            if (!targ.getConfirmations()){
                if (targ.getEdits() == editLimit) {
                    cancelOrder(currOrder);
                } else {
                    ((MeetUpOrder) currOrder).setLocation(location);
                    targ.setEdits(targ.getEdits() + 1);
                    targ.setConfirmations(true);
                    init.setConfirmations(false);
                }
            }
        }
        return currTrade;
    }

    private void cancelOrder(Order currOrder) {
        // currOrder.setOrderState(CANCELED);
    }
}
