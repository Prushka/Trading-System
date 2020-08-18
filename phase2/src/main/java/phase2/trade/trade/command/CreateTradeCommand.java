package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.trade.OrderState;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.UserOrderBundle;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = false, persistent = true)
public class CreateTradeCommand extends TradeCommand<Trade> {

    @Override
    public void execute(ResultStatusCallback<Trade> callback, String... args) {
        if (!checkPermission(callback)) return;
        toUpdate.setLocalDateTime(LocalDateTime.now());
        Set<TradeOrder> ordersToRemove = new HashSet<>();
        for (TradeOrder order : toUpdate.getOrders()) {
            order.setOrderState(OrderState.PENDING_CONFIRMATION);
            if (order.getRightBundle().getTradeItemHolder().size() == 0 && order.getLeftBundle().getTradeItemHolder().size() == 0) {
                ordersToRemove.add(order);
            } else if (order.getDateAndTime() == null) {
                callback.call(null, new StatusFailed("missing.date.and.time"));
                return;
            }
        }
        toUpdate.getOrders().removeAll(ordersToRemove);

        getEntityBundle().getUserOrderBundleGateway().submitTransaction((gateway) -> {
            getEntityBundle().getUserGateway().submitTransaction((userGateway) -> {

                for (TradeOrder order : toUpdate.getOrders()) { // persist UserOrderBundle
                    UserOrderBundle rightBundle = order.getRightBundle();
                    UserOrderBundle leftBundle = order.getLeftBundle();

                    gateway.add(rightBundle);
                    gateway.add(leftBundle);

                    // remove items from their carts if exist
                    // items that are involved in a Trade but not to be traded to a user won't be removed
                    // for example A has 1, 2, 3 and 4 in his/her cart.
                    // B owns 1, C owns 2, D owns 3, E owns 4
                    // A gets 1, 2, B gets 3, C gets 4, etc..
                    // only 1 and 2 will be removed from A's cart, 3 will be removed from B's cart if it is in his/her cart
                    // most logic is in CreateTrade class, not here

                    order.getLeftUser().getCart().removeItemByUid(rightBundle.getTradeItemHolder().getItemsAsIds());
                    order.getRightUser().getCart().removeItemByUid(leftBundle.getTradeItemHolder().getItemsAsIds());
                    userGateway.update(order.getLeftUser());
                    userGateway.update(order.getRightUser());
                }

                getEntityBundle().getTradeGateway().submitTransaction((tradeGateway) -> {
                    tradeGateway.add(toUpdate);
                    if (callback != null)
                        callback.call(toUpdate, new StatusSucceeded());
                }, false);
            }, false);
        });


    }

}
