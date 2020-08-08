package phase2.trade.gateway;

import phase2.trade.command.Command;

import java.util.List;
import java.util.Set;

public interface CommandGateway extends Gateway<Command> {

    List<Command<?>> isUndoable(Set<Long> effectedIds, Long commandTimestamp);

}
