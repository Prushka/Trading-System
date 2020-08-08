package phase2.trade.gateway;

import java.util.List;

public interface Gateway<T> {
    void add(T entity);

    void update(T entity);

    T findById(String id);

    T findById(Long id);

    T findById(Integer id);

    void delete(T entity);

    void delete(Long id);

    List<T> findAll();

    void deleteAll();

    // void submitSessionSync(Runnable runnable);

    // void submitSessionWithTransactionSync(Runnable runnable);

    // void submitSessionAsync(Runnable runnable);

    // void submitSessionWithTransactionAsync(Runnable runnable);

    void submitTransaction(Runnable runnable, boolean asynchronous);

    void submitSession(Runnable runnable, boolean asynchronous);

    void submitTransaction(Runnable runnable);

    void submitSession(Runnable runnable);

    void openCurrentSessionWithTransaction();

    void openCurrentSession();

    void closeCurrentSession();

    void closeCurrentSessionWithTransaction();
}
