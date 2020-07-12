package group.system;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dan Lyu
 */
public class SaveHook {

    private final List<Savable> savables = new ArrayList<>();

    public void save() {
        savables.forEach(Savable::save);
    }

    public void addSavable(Savable savable) {
        savables.add(savable);
    }
}
