package phase2.trade;

import phase2.trade.command.CommandFactory;
import phase2.trade.user.command.CreateUser;

/**
 * Creates prerequisite if not exist.
 *
 * @author Dan Lyu
 */
public class CreatePrerequisiteIfNotExist {

    private final CommandFactory commandFactory;

    /**
     * Constructs a new Create prerequisite if not exist.
     *
     * @param commandFactory the command factory
     */
    public CreatePrerequisiteIfNotExist(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
        createHeadIfNotExist();
    }

    /**
     * Create head if not exist.
     */
    public void createHeadIfNotExist() {
        CreateUser createUser = commandFactory.getCommand(CreateUser::new, true);
        createUser.execute((result, resultStatus) -> {
                },
                "admin", "admin@example.com", "admin???", "HEAD_ADMIN", "Canada", "Ontario", "Toronto");
    }
}
