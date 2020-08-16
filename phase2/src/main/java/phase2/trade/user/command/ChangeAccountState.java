package phase2.trade.user.command;

import phase2.trade.address.Address;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.user.AccountState;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {})
public class ChangeAccountState extends UserCommand<User> {

    @Override
    public void execute(ResultStatusCallback<User> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            operator.setAccountState(AccountState.valueOf(argRequired(0, AccountState.NORMAL.name(), args)));
            gateway.merge(operator);
            save();
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
