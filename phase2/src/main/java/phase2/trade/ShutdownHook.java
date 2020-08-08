package phase2.trade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShutdownHook {

    private List<Shutdownable> shutdownableList = new ArrayList<>();

    public void addShutdownable(Shutdownable... shutdownable) {
        shutdownableList.addAll(Arrays.asList(shutdownable));
    }

    public void shutdown() {
        shutdownableList.forEach(Shutdownable::stop);
    }
}
