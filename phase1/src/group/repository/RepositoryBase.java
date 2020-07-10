package group.repository;

import group.menu.data.Response;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * The implementation of list related operations in {@link Repository}.
 *
 * @param <T> The entity this RepositoryBase handles with
 * @author Dan Lyu
 */
public abstract class RepositoryBase<T extends UniqueId> implements Repository<T> {

    /**
     * The list that holds the entities
     */
    List<T> data;

    /**
     * The file object this Repository reads and saves
     */
    File file;

    /**
     * @param path the path to the file
     */
    public RepositoryBase(String path) {
        this.file = new File(path);
        mkdirs();
    }

    /**
     * a package protected read method, to be implemented by subclasses.<br>
     * This method handles with the real reading operation
     */
    abstract void readSafe();

    /**
     * make this file's parent directories
     */
    private void mkdirs() {
        if (!file.exists()) {
            boolean mkdir = new File(file.getParent()).mkdirs();
        }
    }

    /**
     * The save method to be used by other classes
     */
    public void save() {
        if (data != null && data.size() > 0) {
            saveSafe();
        }
    }

    /**
     * A package protected save method, to be implemented by subclasses.<br>
     * This method handles with the real saving operation
     */
    abstract void saveSafe();

    /**
     * @param entity the entity to be checked
     * @return if the entity exists
     */
    @Override
    public boolean ifExists(T entity) {
        return data.contains(entity);
    }

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

    @Override
    public T getFirst(Filter<T> filter) {
        if (iterator(filter).hasNext()) {
            return iterator(filter).next();
        }
        return null;
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

    @Override
    public boolean ifExists(int id) {
        return id < data.size();
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

}
