package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.TradeGateway;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeConfirmer;
import phase2.trade.trade.TradeCreator;
import phase2.trade.trade.TradeManager;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;

@Entity
public class ConfirmTrade extends TradeCommand<Trade> {

    private Long tradeId;

    private final TradeGateway tradeGateway;

    private TradeConfirmer tcc;

    public ConfirmTrade(GatewayBundle gatewayBundle, RegularUser operator, Long tradeId, Integer timeLimit){
        super(gatewayBundle, operator);
        this.tradeGateway = gatewayBundle.getEntityBundle().getTradeGateway();
        tcc = new TradeConfirmer(timeLimit);
    }

    @Override
    public PermissionSet getPermissionRequired() { return new PermissionSet(Permission.CONFIRM_TRADE); }

    @Override
    public void execute(StatusCallback<Trade> callback, String... args) {
        if (!checkPermission()) {
            callback.call(null, ResultStatus.NO_PERMISSION);
            return;
        }
        getEntityBundle().getTradeGateway().submitTransaction(() -> {
            Trade currTrade = findTradeByIdSyncOutsideTradeGateway(tradeId);
            Trade trade = tcc.confirmTrade(currTrade, operator);
            if (callback != null)
                callback.call(trade, ResultStatus.SUCCEEDED);
            getEntityBundle().getTradeGateway().update(trade);
        });
    }


    // Unreasonable to do for this action
    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    @Override
    public CRUDType getCRUDType() {
        return CRUDType.UPDATE;
    }
}
