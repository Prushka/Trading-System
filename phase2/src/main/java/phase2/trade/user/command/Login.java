package phase2.trade.user.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.support.SupportTicket;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false,
        persistent = false)
public class Login extends UserCommand<User> {

    @Override
    public void execute(ResultStatusCallback<User> callback, String... args) {
        getEntityBundle().getUserGateway().submitSession((gateway) -> {
            List<User> matchedUsers = gateway.findMatches(args[0], args[1]);
            if (matchedUsers.size() > 0) {
                User user = matchedUsers.get(0);
                user.getAvatar().getImageData(); // lazy load
                int size = user.getSupportTickets().size(); // lazy load
                callback.call(user, new StatusSucceeded());
            } else {
                callback.call(null, new StatusFailed());
            }
        });
    }


}
