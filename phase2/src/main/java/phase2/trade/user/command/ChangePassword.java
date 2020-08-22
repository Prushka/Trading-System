package phase2.trade.user.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.user.User;

import javax.persistence.Entity;

/**
 * The Change password.
 *
 * @author Dan Lyu
 */
@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true)
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
            User user = gateway.findById(getOneEntity(User.class));
            user.setPassword(oldPassword);
            gateway.merge(user);
            updateUndo();
        });
    }

    private String getOldPassword() {
        return oldPassword;
    }

    private void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
