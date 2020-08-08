package phase2.trade.user.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.user.User;

import java.util.List;

public class Login extends UserCommand<User> {

    public Login(EntityBundle entityBundle) {
        super(entityBundle);
    }

    @Override
    public void execute(StatusCallback<User> callback, String... args) {
        getUserGateway().submitSession(() -> {
            List<User> matchedUsers = getUserGateway().findMatches(args[0], args[1]);
            if (matchedUsers.size() > 0) {
                User user = matchedUsers.get(0);
                callback.call(user, ResultStatus.SUCCEEDED);
            } else {
                callback.call(null, ResultStatus.FAILED);
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
