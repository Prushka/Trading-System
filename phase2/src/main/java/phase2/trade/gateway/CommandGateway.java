package phase2.trade.gateway;

import phase2.trade.command.Command;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CommandGateway extends EntityGateway<Command> {

    List<Command<?>> getFutureCommands(Long commandTimestamp);

}
