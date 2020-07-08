package group.repository;

/**
 * Use this in lambda or implement a concrete class.
 * This will be used by {@link RepositoryIterator} to filter through the list
 *
 * @param <T> The entity this filter handles with
 *
 * @author Dan Lyu
 */
public interface Filter<T> {

    /**
     * @param entity the entity that needs to be checked
     * @return if certain properties of this entity meets the condition
     */
    boolean match(T entity);

}
