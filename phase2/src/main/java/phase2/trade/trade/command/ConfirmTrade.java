package phase2.trade.trade.command;

import phase2.trade.callback.Callback;
import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.command.Command;
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
import java.util.List;

@Entity
public class ConfirmTrade extends TradeCommand<Trade> {

    private Long tradeId;

    private TradeConfirmer tcc;

    @Override
    public void execute(StatusCallback<Trade> callback, String... args) {
        tcc = new TradeConfirmer(getConfigBundle().getTradeConfig().getTimeLimit());
        if (!checkPermission()) {
            callback.call(null, ResultStatus.NO_PERMISSION);
            return;
        }
        getEntityBundle().getTradeGateway().submitTransaction((gateway) -> {
            Trade currTrade = findTradeByIdSyncOutsideTradeGateway(tradeId);
            Trade trade = tcc.confirmTrade(currTrade, operator);
            if (callback != null)
                callback.call(trade, ResultStatus.SUCCEEDED);
            gateway.update(trade);
        });
    }

    @Override
    public void isUndoable(StatusCallback<List<Command<?>>> callback) {
        callback.call(null, ResultStatus.FAILED);
    }

    // Unreasonable to do for this action
    @Override
    public void undo() {

    }
}
