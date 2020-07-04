package repository;

import menu.data.Response;

public interface ResponseMapper<T> {
    void map(T entity, Response.Builder builder);
}
