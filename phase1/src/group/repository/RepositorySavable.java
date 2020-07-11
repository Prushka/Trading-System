package group.repository;

import group.system.Savable;

/**
 * <p>The interface of a Repository Implementation.</p>
 * List related operations can be found in {@link RepositoryBase}
 *
 * @param <T> The entity type it handles with
 * @author Dan Lyu
 */
public interface RepositorySavable<T extends UniqueId> extends Savable, Repository<T> {

    /**
     * Save operation to save the data it handles with to a file
     */
    void save();

}
