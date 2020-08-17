package phase2.trade.user.command;

import phase2.trade.address.Address;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusExist;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;
import phase2.trade.user.UserFactory;
import phase2.trade.validator.Validator;
import phase2.trade.validator.ValidatorFactory;
import phase2.trade.validator.ValidatorType;

import javax.persistence.Entity;
import java.util.List;

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
