package phase2.trade.user.command;

import phase2.trade.command.CRUDType;
import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.List;

public class Login extends UserCommand<User> {

    public Login(GatewayBundle gatewayBundle) {
        super(gatewayBundle);
    }

    @Override
    public void execute(Callback<User> callback, String... args) {
        getUserGateway().submitSession(() -> {
            List<User> matchedUsers = getUserGateway().findMatches(args[0], args[1]);
            if (matchedUsers.size() > 0) {
                User user = matchedUsers.get(0);
                callback.call(user);
            } else {
                callback.call(null);
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
        return User.class;
    }

    @Override
    public CRUDType getCRUDType() {
        return CRUDType.READ;
    }
}
