package phase2.trade.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class DAO<T> {

    private Session currentSession;

    private Transaction currentTransaction;

    private final Class<T> clazz;

    private final DatabaseResourceBundle databaseResourceBundle;

    public DAO(Class<T> clazz, DatabaseResourceBundle databaseResourceBundle) {
        this.clazz = clazz;
        this.databaseResourceBundle = databaseResourceBundle;
    }

    public void openCurrentSession() {
        currentSession = getSessionFactory().openSession();
    }

    public void openCurrentSessionWithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public void closeCurrentSessionWithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    public SessionFactory getSessionFactory() {
        return databaseResourceBundle.getSessionFactory();
    }

    public ExecutorService getThreadPool() {
        return databaseResourceBundle.getThreadPool();
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void add(T entity) {
        getCurrentSession().save(entity);
    }

    public void update(T entity) {
        getCurrentSession().update(entity);
    }

    public T findById(String id) {
        return getCurrentSession().get(clazz, id);
    }

    public T findById(Long id) {
        return getCurrentSession().get(clazz, id);
    }

    public T findById(Integer id) {
        return getCurrentSession().get(clazz, new Long(id));
    }

    public void delete(T entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return (List<T>) getCurrentSession().createQuery("from " + clazz.getSimpleName()).list();
    }

    public void deleteAll() {
        List<T> entityList = findAll();
        for (T entity : entityList) {
            delete(entity);
        }
    }

    public void submit(Runnable runnable) {
        getThreadPool().submit(runnable);
    }

    public void submitSessionSync(Runnable runnable) {
        openCurrentSession();
        runnable.run();
        closeCurrentSession();
    }

    public void submitSessionWithTransactionSync(Runnable runnable) {
        openCurrentSessionWithTransaction();
        runnable.run();
        closeCurrentSessionWithTransaction();
    }

    public void submitSessionAsync(Runnable runnable) {
        getThreadPool().submit(() -> {
            openCurrentSession();
            runnable.run();
            closeCurrentSession();
        });
    }

    public void submitSessionWithTransactionAsync(Runnable runnable) {
        getThreadPool().submit(() -> {
            openCurrentSessionWithTransaction();
            runnable.run();
            closeCurrentSessionWithTransaction();
        });
    }

    private boolean async = false;

    public void submitSessionWithTransaction(Runnable runnable) {
        if (async) {
            submitSessionWithTransactionAsync(runnable);
        } else {
            submitSessionWithTransactionSync(runnable);
        }
    }

    public void submitSession(Runnable runnable) {
        if (async) {
            submitSessionAsync(runnable);
        } else {
            submitSessionSync(runnable);
        }
    }
}
