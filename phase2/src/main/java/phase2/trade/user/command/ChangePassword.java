package phase2.trade.user.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusExist;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;
import phase2.trade.user.UserFactory;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {Permission.CREATE_USER})
public class ChangePassword extends UserCommand<User> {

    private String oldPassword;

    @Override
    public void execute(ResultStatusCallback<User> callback, String... args) { // username, old password, new password
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            if (gateway.findMatches(args[0], args[1]).size() == 0) {
                callback.call(null, new StatusFailed());
                return;
            }
            oldPassword = operator.getPassword();
            operator.setPassword(args[1]);
            save();
            gateway.update(operator);
            callback.call(operator, new StatusSucceeded());
        });
    }

    @Override
    public void undo() {
        getEntityBundle().getUserGateway().submitTransaction(gateway -> {
            // gateway.delete(userId);
        });
    }
}
