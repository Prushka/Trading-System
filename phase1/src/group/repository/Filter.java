package group.repository;

/**
 * A functional interface used to filter result.
 * Use this in lambda or implement a concrete class.
 * This will be used by {@link RepositoryIterator} to filter through the list
 *
 * @param <T> The entity to be matched
 * @author Dan Lyu
 */

@FunctionalInterface
public interface Filter<T> {

    /**
     * @param entity the entity to be checked
     * @return <code>true</code> if certain properties of this entity meets the condition
     */
    boolean match(T entity);

}
