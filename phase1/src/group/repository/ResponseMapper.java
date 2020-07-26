package group.repository;

import group.menu.data.Response;

/**
 * The functional interface used to map the entity properties to a Response object
 * Use this in lambda, anonymous inner class or implement a concrete class.
 *
 * @param <T> the entity this ResponseMapper deals with
 * @author Dan Lyu
 * @see RepositorySaveImpl#filterResponse(Filter, ResponseMapper)
 */

@FunctionalInterface
public interface ResponseMapper<T> {

    /**
     * @param entity the entity which has the properties that need to be mapped
     * @param builder the {@link Response.Builder} that helps map
     *               the records in {@link RepositoryIterator} to {@link Response} object
     */
    void map(T entity, Response.Builder builder);

}
