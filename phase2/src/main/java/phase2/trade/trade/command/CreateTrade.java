package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.TradeGateway;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeCreator;
import phase2.trade.trade.TradeManager;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class CreateTrade extends TradeCommand<Trade>{

    private final TradeGateway tradeGateway;

    private TradeCreator tc;

    public CreateTrade(GatewayBundle gatewayBundle, RegularUser operator){
        super(gatewayBundle, operator);
        this.tradeGateway = gatewayBundle.getEntityBundle().getTradeGateway();
        tc = new TradeCreator();
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.TRADE);
    }

    @Override
    public void execute(StatusCallback<Trade> callback, String... args) {
        if (!checkPermission()) {
            callback.call(null, ResultStatus.NO_PERMISSION);
            return;
        }
        getEntityBundle().getTradeGateway().submitTransaction(() -> {
            Trade newTrade = tc.createTrade(new ArrayList<>(), new ArrayList<>(), args[0], args[1], args[2], args[3],
                    args[4], args[5], args[6], args[7], args[8], args[9]);
            tradeGateway.add(newTrade);
            if (callback != null)
                callback.call(newTrade, ResultStatus.SUCCEEDED);
        });
    }


    // Unreasonable to do for this action
    @Override
    public void undo() {}

    @Override
    public void redo() {}

    @Override
    public CRUDType getCRUDType() {
        return CRUDType.CREATE;
    }
}
