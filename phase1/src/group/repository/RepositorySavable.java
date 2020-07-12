package group.repository;

import group.system.Savable;

/**
 * The interface of file related operations in Repository.<p>
 * List related operations can be found in {@link Repository}
 *
 * @param <T> The entity type to be used
 * @author Dan Lyu
 */
public interface RepositorySavable<T extends UniqueId> extends Savable, Repository<T> {

    /**
     * Saves the data into a file
     */
    void save();

}
