package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.UserOrderBundle;

import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = false, persistent = true)
public class CreateTradeCommand extends TradeCommand<Trade> {

    @Override
    public void execute(ResultStatusCallback<Trade> callback, String... args) {
        if (!checkPermission(callback)) return;

        Set<TradeOrder> ordersToRemove = new HashSet<>();
        for (TradeOrder order : toUpdate.getOrders()) {
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
