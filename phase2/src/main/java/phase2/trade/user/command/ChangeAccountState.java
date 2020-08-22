package phase2.trade.user.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.command.UpdateCommand;
import phase2.trade.user.AccountState;
import phase2.trade.user.User;

import javax.persistence.Entity;

/**
 * The Change account state.
 *
 * @author Dan Lyu
 */
@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true)
public class ChangeAccountState extends UpdateCommand<AccountState> {

    private AccountState oldAccountState;

    @Override
    public void execute(ResultStatusCallback<AccountState> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            AccountState accountState = AccountState.valueOf(argRequired(0, AccountState.NORMAL.name(), args));
            oldAccountState = operator.getAccountState();
            operator.setAccountState(accountState);
            gateway.merge(operator);
            addEffectedEntity(User.class, operator.getUid());
            save();
            callback.call(accountState, new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getUserGateway().submitTransaction(gateway -> {
            User user = gateway.findById(getOneEntity(User.class));
            user.setAccountState(oldAccountState);
            gateway.update(user);
            updateUndo();
        });
    }

    private AccountState getOldAccountState() {
        return oldAccountState;
    }

    private void setOldAccountState(AccountState oldAccountState) {
        this.oldAccountState = oldAccountState;
    }
}
