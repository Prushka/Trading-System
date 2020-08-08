package phase2.trade.gateway;

public interface GatewayBundle {

    UserGateway getUserGateway();

    ItemGateway getItemGateway();

    TradeGateway getTradeGateway();

    CommandGateway getCommandGateway();

}
