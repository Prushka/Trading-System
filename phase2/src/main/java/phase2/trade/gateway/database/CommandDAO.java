package phase2.trade.gateway.database;

import org.hibernate.query.Query;
import phase2.trade.command.Command;
import phase2.trade.gateway.CommandGateway;

import java.util.*;

public class CommandDAO extends DAO<Command> implements CommandGateway {

    public CommandDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(Command.class, databaseResourceBundle);
    }

    @Override
    public List<Command<?>> getFutureCommands(Long commandTimestamp) {
        Query<Command<?>> query = getCurrentSession().createQuery("FROM Command WHERE timestamp > :commandTimestamp");
        query.setParameter("commandTimestamp", commandTimestamp);
        List<Command<?>> result = query.getResultList();
        return result;
    }
}
