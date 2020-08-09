package phase2.trade.gateway;

import phase2.trade.gateway.database.TradeDAO;

public interface EntityBundle {

    UserGateway getUserGateway();

    ItemGateway getItemGateway();

    CommandGateway getCommandGateway();

    TradeGateway getTradeGateway();
}
