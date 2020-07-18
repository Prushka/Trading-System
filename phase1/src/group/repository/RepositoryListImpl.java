package group.repository;

import group.menu.data.Response;

import java.util.Iterator;
import java.util.List;

public class RepositoryListImpl<T extends UniqueId> implements Repository<T> {

    /**
     * The list that holds the entities
     */
    List<T> data;

    /**
     * @param entity the entity to be checked
     * @return if the entity exists
     */
    @Override
    public boolean ifExists(T entity) {
        return data.contains(entity);
    }

    /**
     * @param filter the filter to be used to match results
     * @return <code>true</code> if the result exists
     */
    @Override
    public boolean ifExists(Filter<T> filter) {
        Iterator<T> iterator = iterator(filter);
        return iterator.hasNext();
    }

    /**
     * @param entity an entity to be added
     */
    @Override
    public void add(T entity) {
        if (ifExists(entity)) {
            return;
        }
        entity.setUid(data.size());
        data.add(entity);
    }

    /**
     * @param id the unique id of this entity
     * @return the entity
     */
    @Override
    public T get(int id) {
        return data.get(id);
    }

    /**
     * @param filter the filter to be used to match results
     * @return the first matched element
     */
    @Override
    public T getFirst(Filter<T> filter) {
        Iterator<T> iterator = iterator(filter);
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    /**
     * @param entity the entity who's unique id is unclear
     * @return the unique id of the entity
     */
    @Override
    public int getId(T entity) {
        return data.indexOf(entity);
    }

    /**
     * @param entity the entity to be removed
     */
    @Override
    public void remove(T entity) {
        data.set(getId(entity), null);
    }

    /**
     * @param id the unique id of the entity to be removed
     */
    @Override
    public void remove(int id) {
        data.set(id, null);
    }

    /**
     * @param filter the filter used to match the result
     * @return the iterator that will use the filter object
     */
    @Override
    public Iterator<T> iterator(Filter<T> filter) {
        return new RepositoryIterator<>(data, filter);
    }

    /**
     * @return the iterator that iterates every element in the Repository
     */
    @Override
    public Iterator<T> iterator() {
        return new RepositoryIterator<>(data);
    }

    /**
     * The helper class used by {@link #filterResponse(Filter, ResponseMapper)}
     *
     * @param iterator the iterator that will use the filter object
     * @param mapper   A {@link ResponseMapper} used to directly map the records in iterator to a Response Object
     * @return the Response object
     */
    private Response mapIterator(Iterator<T> iterator, ResponseMapper<T> mapper) {
        Response.Builder builder = new Response.Builder(true);
        iterator.forEachRemaining(t -> mapper.map(t, builder));
        return builder.build();
    }

    /**
     * @param id the unique id as int to be found
     * @return <code>true</code> if the entity with id exists
     */
    @Override
    public boolean ifExists(int id) {
        return id >= 0 && id < data.size();
    }

    /**
     * @param filter the filter used to match the result
     * @param mapper A {@link ResponseMapper} used to directly map the records in iterator to a Response Object
     * @return the Response object
     */
    @Override
    public Response filterResponse(Filter<T> filter, ResponseMapper<T> mapper) {
        return mapIterator(iterator(filter), mapper);
    }

    /**
     * @param mapper the mapper used to map the iterator results to a Response object
     * @return the Response object
     */
    @Override
    public Response filterResponse(ResponseMapper<T> mapper) {
        return filterResponse(null, mapper);
    }

    /**
     * @return the size of this repository
     */
    @Override
    public int size() {
        return data.size();
    }

    /**
     * @param filter the filter to be used to match results
     * @return the size of the iterator that has all matched results
     */
    @Override
    public int size(Filter<T> filter) {
        Iterator<T> iterator = iterator(filter);
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            iterator.next();
        }
        return i;
    }
}
