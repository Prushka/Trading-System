package phase2.trade.user.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.Command;
import phase2.trade.command.CommandProperty;
import phase2.trade.user.AccountState;

import javax.persistence.Entity;

/**
 * The Change account state.
 *
 * @author Dan Lyu
 */
@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {})
public class ChangeAccountState extends Command<AccountState> {

    @Override
    public void execute(ResultStatusCallback<AccountState> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            AccountState accountState = AccountState.valueOf(argRequired(0, AccountState.NORMAL.name(), args));
            operator.setAccountState(accountState);
            gateway.merge(operator);
            save();
            callback.call(accountState, new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getUserGateway().submitTransaction(gateway -> {
            // gateway.delete(userId);
        });
    }
}
