package phase2.trade.gateway;

import phase2.trade.database.*;

/**
 * The Entity gateway bundle. It contains all entity Gateways.
 *
 * @author Dan Lyu
 */
public class EntityGatewayBundle {

    private final UserGateway userGateway;

    private final ItemGateway itemGateway;

    private final TradeGateway tradeGateway;

    private final CommandGateway commandGateway;

    private final SupportTicketGateway supportTicketGateway;


    /**
     * Constructs a new Entity gateway bundle.
     *
     * @param databaseResourceBundle the database resource bundle
     */
    public EntityGatewayBundle(DatabaseResourceBundle databaseResourceBundle) {
        userGateway = new UserDAO(databaseResourceBundle);

        itemGateway = new ItemDAO(databaseResourceBundle);

        tradeGateway = new TradeDAO(databaseResourceBundle);

        commandGateway = new CommandDAO(databaseResourceBundle);

        supportTicketGateway = new SupportTicketDAO(databaseResourceBundle);
    }

    /**
     * Gets user gateway.
     *
     * @return the user gateway
     */
    public UserGateway getUserGateway() {
        return userGateway;
    }

    /**
     * Gets item gateway.
     *
     * @return the item gateway
     */
    public ItemGateway getItemGateway() {
        return itemGateway;
    }

    /**
     * Gets trade gateway.
     *
     * @return the trade gateway
     */
    public TradeGateway getTradeGateway() {
        return tradeGateway;
    }

    /**
     * Gets support ticket gateway.
     *
     * @return the support ticket gateway
     */
    public SupportTicketGateway getSupportTicketGateway() {
        return supportTicketGateway;
    }

    /**
     * Gets command gateway.
     *
     * @return the command gateway
     */
    public CommandGateway getCommandGateway() {
        return commandGateway;
    }
}
