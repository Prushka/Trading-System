package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.item.Item;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeCreator;
import phase2.trade.trade.TradeOrder;
import phase2.trade.user.User;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = false, persistent = true)
public class CreateTradeCommand extends TradeCommand<Trade> {

    @Override
    public void execute(ResultStatusCallback<Trade> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserOrderBundleGateway().submitTransaction((gateway) -> {
            for (TradeOrder order : toUpdate.getOrders()) {
                gateway.add(order.getInitiator());
                gateway.add(order.getTarget());
            }
            getEntityBundle().getTradeGateway().submitTransaction((tradeGateway) -> {
                tradeGateway.add(toUpdate);
                if (callback != null)
                    callback.call(toUpdate, new StatusSucceeded());
            }, false);
        });


    }

}