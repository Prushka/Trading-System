package phase2.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.StatusSucceeded;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false, persistent = false, permissionSet = {Permission.BROWSE_USER_OPERATIONS})
public class GetCommandsByType<T> extends Command<List<Command<T>>> {

    private Class<T> commandClass;

    @Override
    public void execute(ResultStatusCallback<List<Command<T>>> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getCommandGateway().submitSession((gateway) ->
                callback.call(gateway.findByDType(commandClass), new StatusSucceeded()));
    }

    public void setCommandClass(Class<T> commandClass) {
        this.commandClass = commandClass;
    }
}
