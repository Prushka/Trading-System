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
    public List<Command<?>> isUndoable(Collection<Long> effectedIds, Long commandTimestamp) {
        Query<Command<?>> query = getCurrentSession().createQuery("FROM Command WHERE timestamp > :commandTimestamp");
        query.setParameter("commandTimestamp", commandTimestamp);
        List<Command<?>> result = new ArrayList<>();
        for (Command<?> command : query.getResultList()) {
            if (command.getCRUDType().hasEffect && !Collections.disjoint(effectedIds, command.getEffectedIds())) {
                result.add(command);
            }
        }
        return result;
    }
}
