package phase2.trade.user.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionGroupFactory;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class Register extends UserCommand<User> {

    private Long userId;

    private transient PermissionGroupFactory permissionGroupFactory;

    public Register(EntityBundle entityBundle, PermissionGroupFactory permissionGroupFactory) {
        super(entityBundle);
        this.permissionGroupFactory = permissionGroupFactory;
    }

    public Register() {
        super();
    }

    @Override
    public void execute(StatusCallback<User> callback, String... args) {
        getUserGateway().submitTransaction(() -> {
            List<User> usersByEmail = getUserGateway().findByEmail(args[0]);
            List<User> usersByName = getUserGateway().findByUserName(args[1]);
            if (usersByEmail.size() == 0 && usersByName.size() == 0) {
                User user = new RegularUser(args[0], args[1], args[2], args[3], args[4]);
                user.setUserPermission(permissionGroupFactory.getUserPermission(PermissionGroup.REGULAR));
                getUserGateway().add(user);
                userId = user.getUid();
                addEffectedId(user.getUid());
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
}
