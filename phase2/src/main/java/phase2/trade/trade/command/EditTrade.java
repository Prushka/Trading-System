package phase2.trade.trade.command;

import phase2.trade.address.Address;
import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.TradeGateway;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeEditor;
import phase2.trade.trade.TradeManager;
import phase2.trade.trade.TradeState;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class EditTrade extends TradeCommand<Trade> {
    private Long tradeId;

    private transient TradeEditor te;

    @Override
    public void execute(StatusCallback<Trade> callback, String... args) {
        // TO DO: Implement cancelling after limit
        te = new TradeEditor(getConfigBundle().getTradeConfig().getEditLimit());
        if (!checkPermission()) {
            callback.call(null, ResultStatus.NO_PERMISSION);
            return;
        }
        getEntityBundle().getTradeGateway().submitTransaction((gateway) -> {
            Trade currTrade = findTradeByIdSyncOutsideTradeGateway(tradeId);
            getEntityBundle().getUserGateway().update(operator);
            Trade trade = te.edit(currTrade, operator, args);
            gateway.update(trade);
            this.tradeId = trade.getUid();
            callback.call(trade, new StatusSucceeded());
        });
    }

    @Override
    public void undo() {
        getEntityBundle().getTradeGateway().submitTransaction((gateway) -> {
            gateway.delete(tradeId);
            updateUndo();
        });
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }
}
