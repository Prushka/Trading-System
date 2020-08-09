package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.Command;
import phase2.trade.command.PermissionBased;
import phase2.trade.command.UserPermissionChecker;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.PermissionSet;
import phase2.trade.trade.Trade;
import phase2.trade.user.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public abstract class TradeCommand<T> extends Command<T> implements PermissionBased {

    @OneToOne
    User operator;

    public TradeCommand(GatewayBundle gatewayBundle, User operator) {
        super(gatewayBundle);
        this.operator = operator;
    }

    public TradeCommand() {}

    Trade findTradeByIdSyncInsideTradeGateway(Long tradeId) { return getEntityBundle().getTradeGateway().findById(tradeId);}

    Trade findTradeByIdSyncOutsideTradeGateway(Long tradeId) {
        getEntityBundle().getTradeGateway().openCurrentSession();
        Trade trade = getEntityBundle().getTradeGateway().findById(tradeId);
        getEntityBundle().getTradeGateway().closeCurrentSession();
        return trade;
    }


    @Override
    public Class<Trade> getClassToOperateOn() {
        return Trade.class;
    }

    @Override
    public boolean checkPermission() {
        return new UserPermissionChecker(operator, getPermissionRequired()).checkPermission();
    }

    public boolean checkPermission(StatusCallback<?> statusCallback) {
        boolean result = checkPermission();
        if(!result){
            statusCallback.call(null, ResultStatus.NO_PERMISSION);
        }
        return result;
    }

    @Override
    public abstract PermissionSet getPermissionRequired();
}