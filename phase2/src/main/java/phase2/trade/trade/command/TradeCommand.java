package phase2.trade.trade.command;

import phase2.trade.command.Command;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.PermissionSet;
import phase2.trade.trade.Trade;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
public abstract class TradeCommand<T> extends Command<T> {

    Trade findTradeByIdSyncInsideTradeGateway(Long tradeId) { return getEntityBundle().getTradeGateway().findById(tradeId);}

    Trade findTradeByIdSyncOutsideTradeGateway(Long tradeId) {
        getEntityBundle().getTradeGateway().openCurrentSession();
        Trade trade = getEntityBundle().getTradeGateway().findById(tradeId);
        getEntityBundle().getTradeGateway().closeCurrentSession();
        return trade;
    }

}
