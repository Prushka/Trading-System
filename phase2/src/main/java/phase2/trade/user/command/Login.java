package phase2.trade.user.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false,
        persistent = false)
public class Login extends UserCommand<User> {

    @Override
    public void execute(StatusCallback<User> callback, String... args) {
        getEntityBundle().getUserGateway().submitSession((gateway) -> {
            List<User> matchedUsers = gateway.findMatches(args[0], args[1]);
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


}
