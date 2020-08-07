package phase2.trade.gateway.database;

import phase2.trade.command.Command;

public class CommandDAO<T> extends DAO<Command<T>> {

    public CommandDAO(Class<Command<T>> clazz, DatabaseResourceBundle databaseResourceBundle) {
        super(clazz, databaseResourceBundle);
    }

}
