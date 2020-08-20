package phase2.trade;

import phase2.trade.command.CommandFactory;
import phase2.trade.user.command.CreateUserOperation;

public class CreatePrerequisiteIfNotExist {

    private final CommandFactory commandFactory;

    public CreatePrerequisiteIfNotExist(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        createHeadIfNotExist();
    }

    public void createHeadIfNotExist() {
        CreateUserOperation createUserOperation = commandFactory.getCommand(CreateUserOperation::new, true);
        createUserOperation.execute((result, resultStatus) -> {
                },
                "admin", "admin@example.com", "admin???", "HEAD_ADMIN", "Canada", "Ontario", "Toronto");
    }
}
