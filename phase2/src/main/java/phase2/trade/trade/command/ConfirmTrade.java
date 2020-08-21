package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.trade.Trade;
import phase2.trade.trade.rest.in.peace.TradeConfirmer;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = false, persistent = true)
public class ConfirmTrade extends TradeCommand<Trade> {

    private TradeConfirmer tcc;

    @Override
    public void execute(ResultStatusCallback<Trade> callback, String... args) {
        /*
        tcc = new TradeConfirmer(getConfigBundle().getTradeConfig().getTimeLimit());
        if (!checkPermission(callback)) return;
        getEntityBundle().getTradeGateway().submitTransaction((gateway) -> {
            Trade currTrade = gateway.findById(tradeId);
            tcc.confirmTrade(currTrade, operator);
            System.out.println(operator.getName());
            gateway.update(currTrade);
            if (callback != null)
                callback.call(currTrade, new StatusSucceeded());
        });
         */
    }
}
