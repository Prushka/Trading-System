package phase2.trade.database;

import phase2.trade.command.Command;
import phase2.trade.gateway.CommandGateway;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Command} data access object.
 *
 * @author Dan Lyu
 * @see Command
 */
/*
 public class CommandDAO<C extends Command<Q>, Q> extends DAO<C, CommandGateway<C, Q>> implements CommandGateway<C, Q> {
 public class CommandDAO<T extends Command<?>> extends DAO<T, CommandGateway<T>> implements CommandGateway<T> {
 the problem here is that If I define CommandDAO using the lines above
 The parent class's T would ask for Command<?> / Command<Q> instead of Command
 The first problem encountered was that Class<T> is required to construct DAO<T>, so the
 required class becomes Class<Command<?>> or Class<Command<Q>>
 using Command.class would definitely lose the generic type and the Command<Q> doesn't work either
 that's why only raw type is used here until a workaround is figured out
 https://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
 Also the reason why CommandDAO cannot have a wild card also make some of its use cases unable to use a wildcard
 */
public class CommandDAO extends DAO<Command, CommandGateway> implements CommandGateway {

    /**
     * Constructs a new Command dao.
     *
     * @param databaseResourceBundle the database resource bundle
     */
    public CommandDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(Command.class, databaseResourceBundle);
    }

    @Override
    public List<Command> getFutureCommands(Long commandTimestamp) {
        final List<Command> result = new ArrayList<>();

        criteria((builder, criteria, root) -> {
            Predicate restriction = builder.greaterThan(root.get("timestamp"), commandTimestamp);
            criteria.select(root).where(restriction);
            result.addAll(getCurrentSession().createQuery(criteria).getResultList());
        });
        return result;
    }

    @Override
    public <Q> List<Q> findByDType(Class<Q> clazz) {
        final List<Q> result = new ArrayList<>();

        criteria(clazz, (builder, criteria, root) -> {
            Predicate restriction = builder.greaterThan(root.get("dType"), clazz.getSimpleName());
            criteria.select(root).where(restriction);
            result.addAll(getCurrentSession().createQuery(criteria).getResultList());
        });
        return result;
    }

    @Override
    protected CommandGateway getThis() {
        return this;
    }
}
