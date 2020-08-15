package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeEditor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true, persistent = true)
public class EditTrade extends TradeCommand<Trade> {
    private Long tradeId;

    private transient TradeEditor te;

    @Override
    public void execute(ResultStatusCallback<Trade> callback, String... args) {
        // TO DO: Implement cancelling after limit
        te = new TradeEditor(getConfigBundle().getTradeConfig().getEditLimit());
        if (!checkPermission(callback)) return;
        getEntityBundle().getTradeGateway().submitTransaction((gateway) -> {
            Trade currTrade = findTradeByIdSyncOutsideTradeGateway(tradeId);
            getEntityBundle().getUserGateway().update(operator);
            Trade trade = te.edit(currTrade, operator, args);
            gateway.update(trade);
            callback.call(trade, new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getTradeGateway().submitTransaction((gateway) -> {
            gateway.delete(tradeId);
            updateUndo();
        });
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }
}
