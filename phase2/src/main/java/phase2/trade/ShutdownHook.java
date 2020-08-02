package phase2.trade;

import java.util.ArrayList;
import java.util.List;

public class ShutdownHook {

    private List<Shutdownable> shutdownableList = new ArrayList<>();

    public void addShutdownable(Shutdownable shutdownable) {
        shutdownableList.add(shutdownable);
    }

    public void shutdown() {
        shutdownableList.forEach(Shutdownable::stop);
    }
}
