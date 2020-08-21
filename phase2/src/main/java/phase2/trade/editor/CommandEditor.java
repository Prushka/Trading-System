package phase2.trade.editor;

import phase2.trade.command.Command;
import phase2.trade.config.ConfigBundle;

import java.util.List;

/**
 * The editor used to edit {@link Command}.
 *
 * @author Dan Lyu
 * @see Command
 */
public class CommandEditor extends Editor<Command> {

    /**
     * Constructs a new Command editor.
     *
     * @param entities     the entities
     * @param configBundle the config bundle
     */
    public CommandEditor(List<Command> entities, ConfigBundle configBundle) {
        super(entities, configBundle);
    }

}
