package phase2.trade.database;

import phase2.trade.avatar.Avatar;
import phase2.trade.gateway.*;

public class DAOBundle implements EntityBundle {

    private final UserGateway userDAO;

    private final ItemGateway itemDAO;

    private final TradeGateway tradeDAO;

    private final CommandGateway commandDAO;

    private final SupportTicketGateway supportTicketGateway;


    public DAOBundle(DatabaseResourceBundle databaseResourceBundle) {
        userDAO = new UserDAO(databaseResourceBundle);

        itemDAO = new ItemDAO(databaseResourceBundle);

        tradeDAO = new TradeDAO(databaseResourceBundle);

        commandDAO = new CommandDAO(databaseResourceBundle);

        supportTicketGateway = new SupportTicketDAO(databaseResourceBundle);
    }

    public UserGateway getUserGateway() {
        return userDAO;
    }

    public ItemGateway getItemGateway() {
        return itemDAO;
    }

    public TradeGateway getTradeGateway() {
        return tradeDAO;
    }

    public SupportTicketGateway getSupportTicketGateway() {
        return supportTicketGateway;
    }

    public CommandGateway getCommandGateway() {
        return commandDAO;
    }

}
