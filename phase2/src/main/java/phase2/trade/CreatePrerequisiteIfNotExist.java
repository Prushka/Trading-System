package phase2.trade;

import phase2.trade.command.CommandFactory;
import phase2.trade.user.command.CreateUser;

public class CreatePrerequisiteIfNotExist {

    private final CommandFactory commandFactory;

    public CreatePrerequisiteIfNotExist(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        createHeadIfNotExist();
    }

    public void createHeadIfNotExist() {
        CreateUser createUser = commandFactory.getCommand(CreateUser::new, true);
        createUser.execute((result, resultStatus) -> {
                },
                "admin", "admin@example.com", "admin???", "HEAD_ADMIN", "Canada", "Ontario", "Toronto");
    }
}
