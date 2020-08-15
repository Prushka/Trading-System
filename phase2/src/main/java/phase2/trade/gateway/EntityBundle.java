package phase2.trade.gateway;

public interface EntityBundle {

    UserGateway getUserGateway();

    ItemGateway getItemGateway();

    CommandGateway getCommandGateway();

    TradeGateway getTradeGateway();

    SupportTicketGateway getSupportTicketGateway();
}
