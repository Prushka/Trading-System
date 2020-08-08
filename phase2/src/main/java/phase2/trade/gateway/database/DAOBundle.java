package phase2.trade.gateway.database;

import phase2.trade.gateway.*;

public class DAOBundle implements EntityBundle {

    private final UserDAO userDAO;

    private final ItemDAO itemDAO;

    private final TradeDAO tradeDAO;

    private final CommandDAO commandDAO;

    public DAOBundle(DatabaseResourceBundle databaseResourceBundle) {
        userDAO = new UserDAO(databaseResourceBundle);

        itemDAO = new ItemDAO(databaseResourceBundle);

        tradeDAO = new TradeDAO(databaseResourceBundle);

        commandDAO = new CommandDAO(databaseResourceBundle);
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

    public CommandGateway getCommandGateway() {
        return commandDAO;
    }
}
