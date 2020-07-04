package repository;

import menu.data.Response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class RepositoryBase<T extends UniqueId> implements Repository<T> {

    protected List<T> data;

    @Override
    public void add(T entity) {
        entity.setUid(data.size());
        if (data.contains(entity)) {
            return;
        }
        data.add(entity);
    }

    @Override
    public T get(int id) {
        return data.get(id);
    }

    @Override
    public Iterator<T> iterator(Filter<T> filter) {
        return new RepositoryIterator<>(data, filter);
    }

    private Response mapIterator(Iterator<T> iterator, ResponseMapper<T> mapper) {
        Response.Builder builder = new Response.Builder();
        while (iterator.hasNext()) {
            mapper.map(iterator.next(), builder);
        }
        return builder.build();
    }

    @Override
    public Response filterResponse(Filter<T> filter, ResponseMapper<T> mapper) {
        return mapIterator(iterator(filter), mapper);
    }

}
