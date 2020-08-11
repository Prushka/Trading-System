package phase2.trade.user;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CommandFactory;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.command.CreateUser;

public class CreateHeadIfNotExist {

    public CreateHeadIfNotExist(CommandFactory commandFactory) {
        CreateUser createUser = commandFactory.getCommand(CreateUser::new, true);
        createUser.execute((result, resultStatus) -> {

        }, "admin", "admin@example.com", "admin???", "country", "city", "HEAD_ADMIN");
    }
}
