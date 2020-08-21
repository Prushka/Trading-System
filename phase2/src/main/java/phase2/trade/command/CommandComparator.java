package phase2.trade.command;

import java.util.Comparator;

/**
 * The Command comparator that compares Commands using their ids.
 *
 * @author Dan Lyu
 */
public class CommandComparator implements Comparator<Command<?>> {

    public int compare(Command f1, Command f2) {
        return Long.compare(f1.getTimestamp(), f2.getTimestamp());
    }

}