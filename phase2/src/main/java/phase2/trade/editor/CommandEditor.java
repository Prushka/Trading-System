package phase2.trade.editor;

import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.Command;
import phase2.trade.config.ConfigBundle;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.user.User;

import java.util.List;

public class CommandEditor extends Editor<Command> {

    public CommandEditor(List<Command> entities, ConfigBundle configBundle) {
        super(entities, configBundle);
    }

}
