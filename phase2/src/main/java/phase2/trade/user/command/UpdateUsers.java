package phase2.trade.user.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true)
// The permission of this command depends on the operator and the item's owner
public class UpdateUsers extends UserCommand<List<User>> {

    @Override
    public void execute(ResultStatusCallback<List<User>> callback, String... args) {
        if (!checkPermission(Permission.ManageUsers)) { // the user doesn't have ManageUsers perm, this means he/she might be editing his/her own account
            for (User user : toUpdate) {
                if (!user.getUid().equals(operator.getUid())) { // the user is editing other people's account, directly fire a StatusNoPermission and return
                    checkPermission(callback, Permission.ManageUsers);
                    return;
                } else {
                    if (!checkPermission(callback, Permission.ManagePersonalAccount)) // the user cannot even edit his/her own account
                        return;
                }
            }
        }
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            for (User user : toUpdate) {
                gateway.update(user);
                addEffectedEntity(User.class, user.getUid());
            }
            save();
            if (callback != null)
                callback.call(null, new StatusSucceeded());
        });
    }


    @Override
    protected void undoUnchecked() {
    }
}
