package phase2.trade.gateway;

import java.util.List;
import java.util.function.Consumer;

public interface EntityGateway<T, S extends EntityGateway<T, S>> {
    void add(T entity);

    void update(T entity);

    void merge(T entity);

    void persist(T entity);

    T findById(String id);

    T findById(Long id);

    T findById(Integer id);

    void delete(T entity);

    void delete(Long id);

    List<T> findAll();

    void deleteAll();

    void refresh(T entity);

    void submitTransaction(Consumer<S> consumer, boolean asynchronous);

    void submitSession(Consumer<S> consumer, boolean asynchronous);

    void submitTransaction(Consumer<S> consumer);

    void submitSession(Consumer<S> consumer);

    void openCurrentSessionWithTransaction();

    void openCurrentSession();

    void closeCurrentSession();

    void closeCurrentSessionWithTransaction();
}
