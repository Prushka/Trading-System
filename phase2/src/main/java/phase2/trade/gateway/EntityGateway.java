package phase2.trade.gateway;

import java.util.List;
import java.util.function.Consumer;

/**
 * The Entity gateway interface.
 *
 * @param <T> the entity type
 * @param <S> the self-referencing generic type
 * @author Dan Lyu
 */
public interface EntityGateway<T, S extends EntityGateway<T, S>> {
    /**
     * Adds an entity.
     *
     * @param entity the entity
     */
    void add(T entity);

    /**
     * Updates an entity.
     *
     * @param entity the entity
     */
    void update(T entity);

    /**
     * Merges an entity.
     *
     * @param entity the entity
     */
    void merge(T entity);

    /**
     * Find entity by its id.
     *
     * @param id the id
     * @return the entity
     */
    T findById(String id);

    /**
     * Find entity by its id.
     *
     * @param id the id
     * @return the entity
     */
    T findById(Long id);

    /**
     * Find entity by its id.
     *
     * @param id the id
     * @return the entity
     */
    T findById(Integer id);

    /**
     * Remove the entity.
     *
     * @param entity the entity
     */
    void delete(T entity);

    /**
     * Remove the entity using its id if exists.
     *
     * @param id the id
     */
    void delete(Long id);

    /**
     * Find all entities.
     *
     * @return the list
     */
    List<T> findAll();

    /**
     * Refresh an entity, pulls changes from database.
     *
     * @param entity the entity
     */
    void refresh(T entity);

    /**
     * Submits transaction. Opens Session -> Begins Transaction -> consumes consumer -> Commit Transaction -> Closes Session.
     *
     * @param consumer     the consumer
     * @param asynchronous <code>true</code> if executes asynchronously
     */
    void submitTransaction(Consumer<S> consumer, boolean asynchronous);

    /**
     * Submits transaction. Opens Session -> consumes consumer -> Closes Session.
     *
     * @param consumer     the consumer
     * @param asynchronous <code>true</code> if executes asynchronously
     */
    void submitSession(Consumer<S> consumer, boolean asynchronous);

    /**
     * Submit transaction, asynchronously.
     *
     * @param consumer the consumer
     */
    void submitTransaction(Consumer<S> consumer);

    /**
     * Submit session, asynchronously.
     *
     * @param consumer the consumer
     */
    void submitSession(Consumer<S> consumer);

    //void openCurrentSessionWithTransaction();

    //void openCurrentSession();

    //void closeCurrentSession();

    //void closeCurrentSessionWithTransaction();
}
