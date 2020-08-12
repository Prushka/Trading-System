package phase2.trade.trade.command;

import phase2.trade.callback.*;
import phase2.trade.command.Command;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeConfirmer;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class ConfirmTrade extends TradeCommand<Trade> {

    private Long tradeId;

    private TradeConfirmer tcc;

    @Override
    public void execute(ResultStatusCallback<Trade> callback, String... args) {
        tcc = new TradeConfirmer(getConfigBundle().getTradeConfig().getTimeLimit());
        if (!checkPermission(callback)) return;
        getEntityBundle().getTradeGateway().submitTransaction((gateway) -> {
            Trade currTrade = findTradeByIdSyncOutsideTradeGateway(tradeId);
            Trade trade = tcc.confirmTrade(currTrade, operator);
            if (callback != null)
                callback.call(trade, new StatusSucceeded());
            gateway.update(trade);
        });
    }

    @Override
    public void isUndoable(ResultStatusCallback<List<Command<?>>> callback) {
        callback.call(null, new StatusFailed());
    }

    // Unreasonable to do for this action
    @Override
    public void undo() {

    }
}
