package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeCreator;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class CreateTrade extends TradeCommand<Trade>{

    private transient TradeCreator tc;

    @Override
    public void execute(ResultStatusCallback<Trade> callback, String... args) {
        tc = new TradeCreator();
        if (!checkPermission(callback)) return;
        getEntityBundle().getTradeGateway().submitTransaction((gateway) -> {
            Trade newTrade = tc.createTrade(new ArrayList<>(), new ArrayList<>(), args[0], args[1], args[2], args[3],
                    args[4], args[5], args[6], args[7], args[8], args[9]);
            gateway.add(newTrade);
            if (callback != null)
                callback.call(newTrade, new StatusSucceeded());
        });
    }


    // Unreasonable to do for this action
    @Override
    protected void undoUnchecked() {}
}
