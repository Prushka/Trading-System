package phase2.trade.gateway.database;

import phase2.trade.command.Command;
import phase2.trade.gateway.CommandGateway;

public class CommandDAO extends DAO<Command> implements CommandGateway {

    public CommandDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(Command.class, databaseResourceBundle);
    }

}
