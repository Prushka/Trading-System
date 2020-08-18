package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.trade.Trade;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = false, persistent = true)
public class EditTradeCommand extends TradeCommand<Trade> {

    @Override
    public void execute(ResultStatusCallback<Trade> callback, String... args) {
        if (!checkPermission(callback)) return;

        getEntityBundle().getTradeGateway().submitTransaction((tradeGateway) -> {
            tradeGateway.update(toUpdate);
            if (callback != null)
                callback.call(toUpdate, new StatusSucceeded());
        });

        save();

    }

}
