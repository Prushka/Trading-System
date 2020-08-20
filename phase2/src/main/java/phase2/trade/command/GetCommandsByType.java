package phase2.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false, persistent = false, permissionSet = {Permission.ManageUserOperations})
public class GetCommandsByType<T extends Command<?>> extends Command<List<T>> {

    private Class<T> commandClass;

    @Override
    public void execute(ResultStatusCallback<List<T>> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getCommandGateway().submitSession((gateway) ->
                callback.call(gateway.findByDType(commandClass), new StatusSucceeded()));
    }

    public void setCommandClass(Class<T> commandClass) {
        this.commandClass = commandClass;
    }
}
