package phase2.trade.gateway.database;

import org.hibernate.query.Query;
import phase2.trade.command.Command;
import phase2.trade.gateway.CommandGateway;

import java.util.Set;

public class CommandDAO extends DAO<Command> implements CommandGateway {

    public CommandDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(Command.class, databaseResourceBundle);
    }

    @Override
    public boolean isUndoable(Set<Long> effectedIds) {
        Query query = getCurrentSession().createQuery("FROM Command WHERE effectedIds IN :ids");
        query.setParameter("ids", effectedIds);
        return query.getResultList().size() > 0;
    }
}
