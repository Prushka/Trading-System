package phase2.trade.gateway.database;

import org.hibernate.query.Query;
import phase2.trade.command.Command;
import phase2.trade.gateway.CommandGateway;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandDAO extends DAO<Command> implements CommandGateway {

    public CommandDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(Command.class, databaseResourceBundle);
    }

    private Set<Command.Type> unUndoable = new HashSet<Command.Type>() {{
        add(Command.Type.CREATE);
        add(Command.Type.DELETE);
        add(Command.Type.UPDATE);
    }};

    @Override
    public List<Command<?>> isUndoable(Set<Long> effectedIds, Long commandTimestamp) {
        Query<Command<?>> query = getCurrentSession().createQuery("FROM Command WHERE effectedIds IN :ids AND timestamp > :commandTimestamp");
        query.setParameter("ids", effectedIds);
        query.setParameter("commandTimestamp", commandTimestamp);
        List<Command<?>> result = new ArrayList<>();
        for (Command<?> command : query.getResultList()) {
            if (unUndoable.contains(command.getCommandType())) {
                result.add(command);
            }
        }
        return result;
    }
}
