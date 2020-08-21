package phase2.trade.editor;

/**
 * The functional interface Entity id look up.<p>
 * Used to get the id from the entity without specifying the entity type in the parent class.
 *
 * @param <T> the entity
 * @author Dan Lyu
 */
@FunctionalInterface
public interface EntityIdLookUp<T> {
    /**
     * Gets id.
     *
     * @param entity the entity
     * @return the id
     */
    Long getId(T entity);
}
