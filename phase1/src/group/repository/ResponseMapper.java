package group.repository;

import group.menu.data.Response;

/**
 * Use this in lambda or implement a concrete class.
 * It is used to map the entity properties to a Response object
 *
 * @param <T> the entity this ResponseMapper deals with
 *
 * @author Dan Lyu
 * @see RepositoryBase#filterResponse(Filter, ResponseMapper)
 */
public interface ResponseMapper<T> {

    /**
     * @param entity the entity which has the properties that need to be mapped
     * @param builder the {@link Response.Builder} that helps map
     *               the records in {@link RepositoryIterator} to {@link Response} object
     */
    void map(T entity, Response.Builder builder);

}
