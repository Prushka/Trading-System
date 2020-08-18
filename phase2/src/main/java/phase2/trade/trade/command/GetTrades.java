package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.trade.Trade;

import javax.persistence.Entity;
import java.util.Collection;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false, persistent = false,
        permissionSet = {})
public class GetTrades extends TradeCommand<Collection<Trade>> {

    @Override
    public void execute(ResultStatusCallback<Collection<Trade>> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getTradeGateway().submitSession((gateway) ->
                callback.call(gateway.findByUser(operator), new StatusSucceeded()));
    }

}
