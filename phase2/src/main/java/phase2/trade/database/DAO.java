package phase2.trade.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import phase2.trade.gateway.EntityGateway;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * The data access object base class.
 *
 * @param <T> the entity this DAO handles
 * @param <S> the self-referencing type
 * @author Dan Lyu
 * @author Theodora Fragkouli
 */

// Partially based on the work: https://examples.javacodegeeks.com/enterprise-java/hibernate/hibernate-jpa-dao-example/
public abstract class DAO<T, S extends EntityGateway<T, S>> implements EntityGateway<T, S> {

    private Session currentSession;

    private Transaction currentTransaction;

    private final Class<T> clazz;

    private final DatabaseResourceBundle databaseResourceBundle;

    /**
     * Constructs a new data access object.
     *
     * @param clazz                  the clazz
     * @param databaseResourceBundle the database resource bundle to be injected
     */
    public DAO(Class<T> clazz, DatabaseResourceBundle databaseResourceBundle) {
        this.clazz = clazz;
        this.databaseResourceBundle = databaseResourceBundle;
    }

    private void openCurrentSession() {
        currentSession = getSessionFactory().openSession();
    }

    private void openCurrentSessionWithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
    }

    private void closeCurrentSession() {
        currentSession.close();
    }

    private void closeCurrentSessionWithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    /**
     * Gets session factory.
     *
     * @return the session factory
     */
    private SessionFactory getSessionFactory() {
        return databaseResourceBundle.getSessionFactory();
    }

    /**
     * Gets thread pool.
     *
     * @return the thread pool
     */
    public ExecutorService getThreadPool() {
        return databaseResourceBundle.getThreadPool();
    }

    /**
     * Gets current session.
     *
     * @return the current session
     */
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

    /**
     * Call the overloaded method to consume the {@link TriConsumer} using pre-constructed CriteriaBuilder, CriteriaQuery and Root.
     *
     * @param triConsumer the tri consumer
     */
    protected void criteria(TriConsumer<CriteriaBuilder, CriteriaQuery<T>, Root<T>> triConsumer) {
        this.criteria(clazz, triConsumer);
    }

    /**
     * Criteria.
     *
     * @param <W>         the type parameter of this entity
     * @param clazz       the class of the entity
     * @param triConsumer the tri consumer that consumes pre-constructed CriteriaBuilder, CriteriaQuery and Root.
     */
    protected <W> void criteria(Class<W> clazz, TriConsumer<CriteriaBuilder, CriteriaQuery<W>, Root<W>> triConsumer) {
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<W> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<W> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        triConsumer.consume(criteriaBuilder, criteriaQuery, root);
    }

    /**
     * Execute criteria query by creating a query and get a result list from it.
     *
     * @param <W>      the type parameter
     * @param result   the result
     * @param criteria the criteria
     */
    protected <W> void executeCriteriaQuery(Collection<W> result, CriteriaQuery<W> criteria) {
        result.addAll(getCurrentSession().createQuery(criteria).getResultList());
    }

    public void refresh(T entity) {
        getCurrentSession().refresh(entity);
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

    /**
     * Gets this, returns the most child type of this object, to be used with the self-referencing generic type and {@link Consumer} to consume the DAO itself.
     *
     * @return the this
     */
    protected abstract S getThis();
}
