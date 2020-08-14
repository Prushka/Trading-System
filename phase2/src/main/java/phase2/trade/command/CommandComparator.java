package phase2.trade.command;

import java.lang.reflect.Field;
import java.util.Comparator;

public class CommandComparator implements Comparator<Command> {

    public int compare(Command f1, Command f2) {
        return Long.compare(f1.getTimestamp(), f2.getTimestamp());
    }

}