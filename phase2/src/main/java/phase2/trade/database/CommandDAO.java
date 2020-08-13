package phase2.trade.database;

import org.hibernate.query.Query;
import phase2.trade.command.Command;
import phase2.trade.gateway.CommandGateway;

import java.util.*;

public class CommandDAO extends DAO<Command, CommandGateway> implements CommandGateway {

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

    @Override
    public <C> List<Command<C>> findByDType(Class<C> clazz) {
        Query<Command<C>> query = getCurrentSession().createQuery("FROM Command WHERE dType = :dType");
        query.setParameter("dType", clazz.getSimpleName());
        List<Command<C>> result = query.getResultList();
        return result;
    }

    @Override
    protected CommandGateway getThis() {
        return this;
    }
}
