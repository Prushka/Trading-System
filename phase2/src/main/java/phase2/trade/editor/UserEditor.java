package phase2.trade.editor;

import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.config.ConfigBundle;
import phase2.trade.item.Item;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.user.AccountState;
import phase2.trade.user.User;

import java.util.List;

public class UserEditor extends Editor<User> {

    public UserEditor(List<User> entities, ConfigBundle configBundle) {
        super(entities, configBundle);
    }

    public void alterName(String name, StatusCallback statusCallback) {
        entities.forEach(entity -> entity.setName(name));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterEmail(String email, StatusCallback statusCallback) {
        entities.forEach(entity -> entity.setEmail(email));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterPassword(String password, StatusCallback statusCallback) {
        entities.forEach(entity -> entity.setReputation(Integer.parseInt(password)));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterAccountState(String accountState, StatusCallback statusCallback) {
        entities.forEach(entity -> entity.setAccountState(AccountState.valueOf(accountState)));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterReputation(String reputation, StatusCallback statusCallback) {
        if (!isInt(reputation)) {
            statusCallback.call(new StatusFailed());
            return;
        }
        entities.forEach(entity -> entity.setReputation(Integer.parseInt(reputation)));
        statusCallback.call(new StatusSucceeded());
    }


    public void alterPermissionGroup(String s, StatusCallback statusCallback) {
        PermissionGroup permissionGroup = PermissionGroup.valueOf(s);
        entities.forEach(entity -> {
            entity.setPermissionGroup(permissionGroup);
            entity.setUserPermission(configBundle.getPermissionConfig().getDefaultPermissions().get(permissionGroup));
        });
        statusCallback.call(new StatusSucceeded());
    }
}
