package group.repository;

import group.menu.data.Response;

import java.util.Iterator;

public interface Repository<T extends UniqueId> {

    /**
     * @param entity an entity to be added
     */
    void add(T entity);

    /**
     * @param id the unique id of this entity
     * @return the entity
     */
    T get(int id);

    /**
     * @param entity the entity to be checked
     * @return if this entity exists in this Repository
     */
    boolean ifExists(T entity);

    boolean ifExists(Filter<T> filter);

    boolean ifExists(int id);

    T getFirst(Filter<T> filter);

    void remove(T entity);

    void remove(int id);

    int getId(T entity);

    /**
     * @param filter the filter used to match the result
     * @return the iterator that will use the filter object
     */
    Iterator<T> iterator(Filter<T> filter);

    /**
     * @param filter the filter used to match the result
     * @param mapper A {@link ResponseMapper} used to directly map the records in iterator to a Response Object
     * @return the Response object
     */
    Response filterResponse(Filter<T> filter, ResponseMapper<T> mapper);

}
