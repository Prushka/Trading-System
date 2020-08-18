package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;

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
            for (TradeOrder order : toUpdate.getOrders()) {
                gateway.add(order.getLeftBundle());
                gateway.add(order.getRightBundle());
            }
            getEntityBundle().getTradeGateway().submitTransaction((tradeGateway) -> {
                tradeGateway.add(toUpdate);
                if (callback != null)
                    callback.call(toUpdate, new StatusSucceeded());
            }, false);
        });


    }

}
