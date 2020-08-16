package phase2.trade.trade.command;

import phase2.trade.callback.*;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.Command;
import phase2.trade.command.CommandProperty;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeConfirmer;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = false, persistent = true)
public class ConfirmTrade extends TradeCommand<Trade> {

    private TradeConfirmer tcc;

    @Override
    public void execute(ResultStatusCallback<Trade> callback, String... args) {
        tcc = new TradeConfirmer(getConfigBundle().getTradeConfig().getTimeLimit());
        if (!checkPermission(callback)) return;
        getEntityBundle().getTradeGateway().submitTransaction((gateway) -> {
            Trade currTrade = gateway.findById(tradeId);
            Trade trade = tcc.confirmTrade(currTrade, operator);
            if (callback != null)
                callback.call(trade, new StatusSucceeded());
            gateway.update(trade);
        });
    }

    // Unreasonable to do for this action
    @Override
    protected void undoUnchecked() {}
}
