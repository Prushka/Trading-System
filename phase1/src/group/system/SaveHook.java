package group.system;

import java.util.ArrayList;
import java.util.List;

/**
 * Saves a bunch of savable objects (observers) on request.<p>
 * SaveHook is currently used in Shutdownable object.<p>
 *
 * @author Dan Lyu
 */
public class SaveHook {

    /**
     * The list of objects that are savable.
     */
    private final List<Savable> savables = new ArrayList<>();

    /**
     * Saves all objects in {@link #savables}
     */
    public void save() {
        savables.forEach(Savable::save);
    }

    /**
     * @param savable the savable object
     */
    public void addSavable(Savable savable) {
        savables.add(savable);
    }
}
