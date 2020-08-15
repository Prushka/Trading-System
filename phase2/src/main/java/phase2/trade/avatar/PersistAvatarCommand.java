package phase2.trade.avatar;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusExist;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.Command;
import phase2.trade.command.CommandProperty;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;
import phase2.trade.user.UserFactory;
import phase2.trade.user.command.UserCommand;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = false,
        persistent = true, permissionSet = {})
public class PersistAvatarCommand extends Command<Avatar> {

    private transient Avatar avatar;

    @Override
    public void execute(ResultStatusCallback<Avatar> callback, String... args) { // username, email, password, permission_group
        if (!checkPermission(callback)) return;
        getEntityBundle().getAvatarGateway().submitTransaction((gateway) -> {
            gateway.add(avatar);
            callback.call(avatar,new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getUserGateway().submitTransaction(gateway -> {
            gateway.delete(getOneEntity(User.class));
            updateUndo();
        });
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }
}
