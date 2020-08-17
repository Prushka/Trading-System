package phase2.trade.gateway;

import phase2.trade.database.*;

public class EntityGatewayBundle {

    private final UserGateway userGateway;

    private final ItemGateway itemGateway;

    private final TradeGateway tradeGateway;

    private final CommandGateway commandGateway;

    private final SupportTicketGateway supportTicketGateway;

    private final UserOrderBundleGateway userOrderBundleGateway;


    public EntityGatewayBundle(DatabaseResourceBundle databaseResourceBundle) {
        userGateway = new UserDAO(databaseResourceBundle);

        itemGateway = new ItemDAO(databaseResourceBundle);

        tradeGateway = new TradeDAO(databaseResourceBundle);

        commandGateway = new CommandDAO(databaseResourceBundle);

        supportTicketGateway = new SupportTicketDAO(databaseResourceBundle);

        userOrderBundleGateway = new UserOrderBundleDAO(databaseResourceBundle);
    }

    public UserGateway getUserGateway() {
        return userGateway;
    }

    public ItemGateway getItemGateway() {
        return itemGateway;
    }

    public TradeGateway getTradeGateway() {
        return tradeGateway;
    }

    public SupportTicketGateway getSupportTicketGateway() {
        return supportTicketGateway;
    }

    public CommandGateway getCommandGateway() {
        return commandGateway;
    }

    public UserOrderBundleGateway getUserOrderBundleGateway() {
        return userOrderBundleGateway;
    }
}
