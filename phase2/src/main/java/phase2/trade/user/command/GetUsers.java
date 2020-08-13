package phase2.trade.user.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.Command;
import phase2.trade.command.CommandProperty;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false, persistent = false,
        permissionSet = {Permission.BROWSE_USER_OPERATIONS})
public class GetUsers extends Command<List<User>> {

    @Override
    public void execute(ResultStatusCallback<List<User>> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitSession((gateway) ->
                callback.call(gateway.findAll(), new StatusSucceeded()));
    }

}
