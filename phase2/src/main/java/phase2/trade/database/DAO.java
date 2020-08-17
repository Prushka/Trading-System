package phase2.trade.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import phase2.trade.gateway.EntityGateway;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public abstract class DAO<T, S extends EntityGateway<T, S>> implements EntityGateway<T, S> {

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

    @Override
    public void add(T entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(T entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public void merge(T entity) {
        getCurrentSession().merge(entity);
    }

    @Override
    public void persist(T entity) {
        getCurrentSession().persist(entity);
    }

    @Override
    public T findById(String id) {
        return getCurrentSession().get(clazz, id);
    }

    @Override
    public T findById(Long id) {
        return getCurrentSession().get(clazz, id);
    }

    @Override
    public T findById(Integer id) {
        return getCurrentSession().get(clazz, new Long(id));
    }

    @Override
    public void delete(T entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public void delete(Long id) {
        getCurrentSession().delete(findById(id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return (List<T>) getCurrentSession().createQuery("from " + clazz.getSimpleName()).list();
    }

    protected void criteria(TriConsumer<CriteriaBuilder, CriteriaQuery<T>, Root<T>> triConsumer) {
        this.criteria(clazz, triConsumer);
    }

    protected <W> void criteria(Class<W> clazz, TriConsumer<CriteriaBuilder, CriteriaQuery<W>, Root<W>> triConsumer) {
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<W> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<W> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        triConsumer.consume(criteriaBuilder, criteriaQuery, root);
    }

    protected void executeCriteriaQuery(List<T> result, CriteriaQuery<T> criteria) {
        result.addAll(getCurrentSession().createQuery(criteria).getResultList());
    }

    public void refresh(T entity) {
        getCurrentSession().refresh(entity);
    }


    @Override
    public void deleteAll() {
        List<T> entityList = findAll();
        for (T entity : entityList) {
            delete(entity);
        }
    }

    public void submit(Consumer<S> consumer) {
        getThreadPool().submit(() -> consumer.accept(getThis()));
    }

    private void submitSessionSync(Consumer<S> consumer) {
        openCurrentSession();
        consumer.accept(getThis());
        closeCurrentSession();
    }

    private void submitTransactionSync(Consumer<S> consumer) {
        openCurrentSessionWithTransaction();
        consumer.accept(getThis());
        closeCurrentSessionWithTransaction();
    }

    public void submitSession(Consumer<S> consumer, boolean asynchronous) {
        if (asynchronous) {
            getThreadPool().submit(() -> submitSessionSync(consumer));
        } else {
            submitSessionSync(consumer);
        }
    }

    public void submitTransaction(Consumer<S> consumer, boolean asynchronous) {
        if (asynchronous) {
            getThreadPool().submit(() -> submitTransactionSync(consumer));
        } else {
            submitTransactionSync(consumer);
        }
    }

    private final boolean async = false;

    @Override
    public void submitTransaction(Consumer<S> consumer) {
        submitTransaction(consumer, async);
    }

    @Override
    public void submitSession(Consumer<S> consumer) {
        submitSession(consumer, async);
    }

    protected abstract S getThis();
}
