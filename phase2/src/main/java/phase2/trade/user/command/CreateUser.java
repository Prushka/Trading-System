package phase2.trade.user.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.command.PermissionBased;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionGroupFactory;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class CreateUser extends UserCommand<User> implements PermissionBased { // create user by admin / system

    private Long userId;

    private transient PermissionGroupFactory permissionGroupFactory;

    public CreateUser(GatewayBundle gatewayBundle, User operator) {
        super(gatewayBundle, operator);
        this.permissionGroupFactory = new PermissionGroupFactory(gatewayBundle.getConfigBundle().getPermissionConfig());
    }

    public CreateUser() {
        super();
    }

    @Override
    public void execute(StatusCallback<User> callback, String... args) { // username, email, password, permission_group
        getUserGateway().submitTransaction(() -> {
            List<User> usersByEmail = getUserGateway().findByEmail(args[0]);
            List<User> usersByName = getUserGateway().findByUserName(args[0]);
            if (usersByEmail.size() == 0 && usersByName.size() == 0) {
                User user = new RegularUser(args[0], args[1], args[2], args[3], args[4]);
                user.setUserPermission(permissionGroupFactory.getUserPermission(PermissionGroup.REGULAR));
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

    @Override
    public void redo() {

    }

    @Override
    public Class<?> getClassToOperateOn() {
        return null;
    }

    @Override
    public CRUDType getCRUDType() {
        return null;
    }

    @Override
    public boolean checkPermission() {
        return false;
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.CREATE_USER);
    }
}
