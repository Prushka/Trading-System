package phase2.trade.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false, persistent = false, permissionSet = {Permission.BROWSE_USER_OPERATIONS})
public class GetCommands extends Command<List<Command>> {

    @Override
    public void execute(StatusCallback<List<Command>> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        getEntityBundle().getCommandGateway().submitSession((gateway) ->
                callback.call(gateway.findAll(), new StatusSucceeded()));
    }


    @Override
    public void undo() {
    }

}
