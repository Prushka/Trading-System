package phase2.trade.user.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;
import phase2.trade.user.UserFactory;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = true,
        persistent = true, permissionSet = {Permission.CREATE_USER})
public class CreateUser extends UserCommand<User> {

    private Long userId;

    public CreateUser(GatewayBundle gatewayBundle, User operator) {
        super(gatewayBundle, operator);
    }

    public CreateUser(GatewayBundle gatewayBundle) {
        super(gatewayBundle);
    }

    public CreateUser() {}

    @Override
    public void execute(StatusCallback<User> callback, String... args) { // username, email, password, permission_group
        if (!checkPermission(callback)) {
            return;
        }
        getUserGateway().submitTransaction(() -> {
            List<User> usersByName = getUserGateway().findByUserName(args[0]);
            List<User> usersByEmail = getUserGateway().findByEmail(args[1]);
            if (usersByEmail.size() == 0 && usersByName.size() == 0) {
                User user = new UserFactory(gatewayBundle.getConfigBundle().getPermissionConfig()).createByPermissionGroup(args[0], args[1], args[2], args[3], args[4], args[5]);
                getUserGateway().add(user);
                userId = user.getUid();
                addEffectedEntity(User.class, user.getUid());
                save();
                callback.call(user, ResultStatus.SUCCEEDED);
            } else {
                callback.call(null, ResultStatus.EXIST);
            }
        });
    }

    @Override
    public void undo() {

    }
}
