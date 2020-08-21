package phase2.trade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Shutdown hook.
 *
 * @author Dan Lyu
 */
public class ShutdownHook {

    private final List<Shutdownable> shutdownableList = new ArrayList<>();

    /**
     * Add shutdownables.
     *
     * @param shutdownable the shutdownable
     */
    public void addShutdownables(Shutdownable... shutdownable) {
        shutdownableList.addAll(Arrays.asList(shutdownable));
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        shutdownableList.forEach(Shutdownable::stop);
        // System.exit(0);
    }
}
