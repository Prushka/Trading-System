package phase2.trade.user.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {})
public class ChangePassword extends UserCommand<User> {

    private String oldPassword;

    @Override
    public void execute(ResultStatusCallback<User> callback, String... args) { // username, old password, new password
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            if (gateway.findMatches(args[0], args[1]).size() == 0) {
                callback.call(null, new StatusFailed("wrong.password"));
                return;
            }
            oldPassword = operator.getPassword();
            operator.setPassword(args[2]);
            save();
            gateway.merge(operator);
            callback.call(operator, new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getUserGateway().submitTransaction(gateway -> {
            // gateway.delete(userId);
        });
    }
}
