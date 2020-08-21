package phase2.trade.user.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusExist;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.List;

/**
 * The Change user name.
 *
 * @author Dan Lyu
 */
@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {})
public class ChangeUserName extends UserCommand<User> {

    private String oldName;

    @Override
    public void execute(ResultStatusCallback<User> callback, String... args) { // old user name, old password, new user name
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            if (gateway.findMatches(args[0], args[1]).size() == 0) {
                callback.call(null, new StatusFailed("wrong.password"));
                return;
            }

            List<User> usersByName = gateway.findByUserName(args[2]);
            if (usersByName.size() > 0) {
                callback.call(null, new StatusExist("user.name.exists"));
                return;
            }
            oldName = operator.getName();
            operator.setName(args[2]);
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
