package phase2.trade.gateway;

import java.util.List;

public interface Gateway<T> {
    void add(T entity);

    void update(T entity);

    T findById(String id);

    T findById(Long id);

    T findById(Integer id);

    void delete(T entity);

    List<T> findAll();

    void deleteAll();

    void submitSessionSync(Runnable runnable);

    void submitSessionWithTransactionSync(Runnable runnable);

    void submitSessionAsync(Runnable runnable);

    void submitSessionWithTransactionAsync(Runnable runnable);

    void submitSessionWithTransaction(Runnable runnable);

    void submitSession(Runnable runnable);

    void openCurrentSessionWithTransaction();

    void openCurrentSession();

    void closeCurrentSession();

    void closeCurrentSessionWithTransaction();
}
