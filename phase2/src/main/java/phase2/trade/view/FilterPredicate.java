package phase2.trade.view;

/**
 * The interface Filter predicate.
 *
 * @param <T> the entity type
 * @param <E> the adapted representation of the entity (for example it's a String in TextField)
 * @author Dan Lyu
 */
public interface FilterPredicate<T, E> {
    /**
     * Test.
     *
     * @param entity  the entity
     * @param toMatch the to match
     * @return <code>true</code> if matches
     */
    boolean test(T entity, E toMatch);
}
