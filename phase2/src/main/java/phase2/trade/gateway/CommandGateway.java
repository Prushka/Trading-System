package phase2.trade.gateway;

import phase2.trade.command.Command;

import java.util.List;

/**
 * The {@link Command} gateway interface.
 *
 * @author Dan Lyu
 * @see Command
 */
// public interface CommandGateway<C extends Command<Q>,Q> extends EntityGateway<C, CommandGateway<C,Q>> {
public interface CommandGateway extends EntityGateway<Command, CommandGateway> {

    /**
     * Gets commands that have a later timestamp than the current one.
     *
     * @param commandTimestamp the command timestamp
     * @return the future commands
     */
    List<Command> getFutureCommands(Long commandTimestamp);

    /**
     * Find by all commands depending on the their types.
     *
     * @param <Q>   the type of the Command
     * @param clazz the Command class
     * @return the list that contain all such Commands
     */
    <Q> List<Q> findByDType(Class<Q> clazz);
}
