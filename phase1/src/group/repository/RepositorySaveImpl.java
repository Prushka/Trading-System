package group.repository;

import group.config.LoggerFactory;
import group.system.Savable;
import group.system.SaveHook;

import java.io.File;
import java.util.logging.Logger;

/**
 * The implementation of list related operations in {@link Repository}.
 *
 * @param <T> The entity type to be used
 * @author Dan Lyu
 */
public abstract class RepositorySaveImpl<T extends UniqueId> extends RepositoryListImpl<T> implements Savable {

    Logger LOGGER = new LoggerFactory(RepositorySaveImpl.class).getConfiguredLogger();

    /**
     * The file object this Repository reads to and saves from
     */
    final File file;

    /**
     * @param path     the path to the file
     * @param saveHook the repository will be saved by a saveHook
     */
    public RepositorySaveImpl(String path, SaveHook saveHook) {
        this.file = new File(path);
        saveHook.addSavable(this);
        mkdirs();
    }

    /**
     * make this file's parent directories
     */
    private void mkdirs() {
        if (!file.exists()) {
            boolean mkdir = new File(file.getParent()).mkdirs();
        }
    }

}
