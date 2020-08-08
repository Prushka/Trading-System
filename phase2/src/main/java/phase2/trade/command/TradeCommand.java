package phase2.trade.command;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.trade.Trade;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
public class TradeCommand extends Command<Trade>{
    public TradeCommand(GatewayBundle gatewayBundle) {
        super(gatewayBundle);
    }

    public TradeCommand() {

    }

    @Override
    public void execute(Callback<Trade> callback, String... args) {

    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    @Override
    public Class<?> getClassToOperateOn() {
        return null;
    }

    @Override
    public CRUDType getCRUDType() {
        return null;
    }
}
