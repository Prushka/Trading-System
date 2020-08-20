package phase2.trade.gateway;

import phase2.trade.command.Command;

import java.util.List;

// public interface CommandGateway<C extends Command<Q>,Q> extends EntityGateway<C, CommandGateway<C,Q>> {
public interface CommandGateway extends EntityGateway<Command, CommandGateway> {

    List<Command> getFutureCommands(Long commandTimestamp);

    <Q> List<Q> findByDType(Class<Q> clazz);
}
