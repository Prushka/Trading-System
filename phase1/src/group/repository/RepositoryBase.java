package group.repository;

import group.menu.data.Response;
import group.system.SaveHook;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * The implementation of list related operations in {@link Repository}.
 *
 * @param <T> The entity type to be used
 * @author Dan Lyu
 */
public abstract class RepositoryBase<T extends UniqueId> implements RepositorySavable<T> {

    /**
     * The list that holds the entities
     */
    List<T> data;

    /**
     * The file object this Repository reads to and saves from
     */
    final File file;

    /**
     * @param path     the path to the file
     * @param saveHook the repository will be saved by a saveHook
     */
    public RepositoryBase(String path, SaveHook saveHook) {
        this.file = new File(path);
        saveHook.addSavable(this);
        mkdirs();
    }

    /**
     * make this file's parent directories
     */
    private void mkdirs() {
        if (!file.exists()) {
            boolean mkdir = new File(file.getParent()).mkdirs();
        }
    }

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

    /**
     * @param id the unique id of this entity
     * @return the entity
     */
    @Override
    public T get(Long id) {
        return get(id.intValue());
    }

    @Override
    public T getFirst(Filter<T> filter) {
        if (iterator(filter).hasNext()) {
            return iterator(filter).next();
        }
        return null;
    }

    @Override
    public int getId(T entity) {
        return data.indexOf(entity);
    }

    @Override
    public void remove(T entity) {
        data.set(getId(entity), null);
    }

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
     * @param id the unique id as Long to be found
     * @return <code>true</code> if the entity with id exists
     */
    @Override
    public boolean ifExists(Long id) {
        return ifExists(id.intValue());
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

    @Override
    public int size() {
        return data.size();
    }

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
