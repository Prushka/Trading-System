package phase2.trade.user.command;

import phase2.trade.command.CRUDType;
import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.List;

public class Register extends UserCommand<User> {

    public Register(GatewayBundle gatewayBundle) {
        super(gatewayBundle);
    }

    @Override
    public void execute(Callback<User> callback, String... args) {
        getUserGateway().submitTransaction(() -> {
            List<User> usersByEmail = getUserGateway().findByEmail(args[0]);
            List<User> usersByName = getUserGateway().findByUserName(args[1]);
            if (usersByEmail.size() == 0 && usersByName.size() == 0) {
                User user = new PersonalUser(args[0], args[1], args[2], args[3], args[4]);
                getUserGateway().add(user);
                callback.call(user);
            }
            callback.call(null);
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
