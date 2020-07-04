package repository;

public interface Filter<T> {

    boolean match(T entity);

}
