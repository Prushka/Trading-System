package phase2.trade.user;

import phase2.trade.command.CommandFactory;
import phase2.trade.controller.ControllerResources;
import phase2.trade.gateway.ConfigBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.command.CreateUser;

public class FirstCheck {

    private final CommandFactory commandFactory;

    public FirstCheck(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        createHeadIfNotExist();
    }

    public void createHeadIfNotExist() {
        CreateUser createUser = commandFactory.getCommand(CreateUser::new, true);
        createUser.execute((result, resultStatus) -> {
                },
                "admin", "admin@example.com", "admin???", "country", "city", "HEAD_ADMIN");
    }
}
