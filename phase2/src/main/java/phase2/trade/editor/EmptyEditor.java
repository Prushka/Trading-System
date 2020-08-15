package phase2.trade.editor;

import phase2.trade.command.Command;
import phase2.trade.config.ConfigBundle;

import java.util.List;

public class EmptyEditor<T> extends Editor<T> {

    public EmptyEditor(List<T> entities, ConfigBundle configBundle) {
        super(entities, configBundle);
    }

}
