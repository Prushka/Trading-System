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

    private final TradeGateway tradeGateway;

    private Integer editLimit;

    private TradeEditor te;

    public EditTrade(GatewayBundle gatewayBundle, RegularUser operator, Long tradeId, int editLimit){
        super(gatewayBundle, operator);
        this.tradeGateway = gatewayBundle.getEntityBundle().getTradeGateway();
        this.editLimit = editLimit;
        te = new TradeEditor(editLimit);
        this.tradeId = tradeId;
    }

    @Override
    public PermissionSet getPermissionRequired() { return new PermissionSet(Permission.EDIT_TRADE); }

    @Override
    public void execute(StatusCallback<Trade> callback, String... args) {
        // TO DO: Implement cancelling after limit
        if (!checkPermission()) {
            callback.call(null, ResultStatus.NO_PERMISSION);
            return;
        }
        getEntityBundle().getTradeGateway().submitTransaction(() -> {
            Trade currTrade = findTradeByIdSyncOutsideTradeGateway(tradeId);
            getEntityBundle().getUserGateway().update(operator);
            Trade trade = te.edit(currTrade, operator, args);
            tradeGateway.update(trade);
            this.tradeId = trade.getUid();
            callback.call(trade, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public void undo() {
        getEntityBundle().getTradeGateway().submitTransaction(() -> {
            getEntityBundle().getTradeGateway().delete(tradeId);
            updateUndo();
        });
    }

    @Override
    public void redo() {
    }

    @Override
    public CRUDType getCRUDType() {
        return CRUDType.UPDATE;
    }
}
