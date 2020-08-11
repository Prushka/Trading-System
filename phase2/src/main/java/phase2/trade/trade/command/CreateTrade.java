package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.TradeGateway;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeCreator;
import phase2.trade.trade.TradeManager;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class CreateTrade extends TradeCommand<Trade>{

    private transient TradeCreator tc;

    @Override
    public void execute(StatusCallback<Trade> callback, String... args) {
        tc = new TradeCreator();
        if (!checkPermission(callback)) {
            return;
        }
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
    public void undo() {}
}
