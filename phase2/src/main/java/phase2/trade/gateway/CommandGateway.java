package phase2.trade.gateway;

import phase2.trade.command.Command;

import java.util.Set;

public interface CommandGateway extends Gateway<Command> {

    boolean isUndoable(Set<Long> effectedIds);

}
