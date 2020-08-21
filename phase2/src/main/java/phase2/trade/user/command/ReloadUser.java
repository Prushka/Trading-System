package phase2.trade.user.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.user.User;

import javax.persistence.Entity;

/**
 * The Reload user.
 *
 * @author Dan Lyu
 */
@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = false,
        persistent = true, permissionSet = {})
public class ReloadUser extends UserCommand<User> {

    @Override
    public void execute(ResultStatusCallback<User> callback, String... args) { // username, email, password, permission_group, country, province, city,
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            gateway.refresh(operator);
            callback.call(operator, new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getUserGateway().submitTransaction(gateway -> {
            gateway.delete(getOneEntity(User.class));
            updateUndo();
        });
    }
}
