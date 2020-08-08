package phase2.trade.gateway;

import phase2.trade.command.Command;

import java.util.Collection;
import java.util.List;

public interface CommandGateway extends EntityGateway<Command> {

    List<Command<?>> isUndoable(Collection<Long> effectedIds, Long commandTimestamp);

}
