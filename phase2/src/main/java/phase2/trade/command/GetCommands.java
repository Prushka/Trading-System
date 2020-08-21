package phase2.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;
import java.util.List;

/**
 * The GetCommands command, used to fetch a list of all Commands from gateway.
 *
 * @author Dan Lyu
 */
@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false, persistent = false, permissionSet = {Permission.ManageUserOperations})
public class GetCommands extends Command<List<Command>> {

    @Override
    public void execute(ResultStatusCallback<List<Command>> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getCommandGateway().submitSession((gateway) ->
                callback.call(gateway.findAll(), new StatusSucceeded()));
    }

}
