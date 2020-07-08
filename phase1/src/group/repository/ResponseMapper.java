package group.repository;

import group.menu.data.Response;

public interface ResponseMapper<T> {
    void map(T entity, Response.Builder builder);
}
