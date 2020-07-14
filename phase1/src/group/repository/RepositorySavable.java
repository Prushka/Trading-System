package group.repository;

import group.config.LoggerFactory;
import group.system.Savable;

import java.util.logging.Logger;

/**
 * The interface of file related operations in Repository.<p>
 * List related operations can be found in {@link Repository}
 *
 * @param <T> The entity type to be used
 * @author Dan Lyu
 */
public interface RepositorySavable<T extends UniqueId> extends Savable, Repository<T> {

    Logger LOGGER = new LoggerFactory(RepositorySavable.class).getConfiguredLogger();

    /**
     * Saves the data into a file
     */
    void save();

}
