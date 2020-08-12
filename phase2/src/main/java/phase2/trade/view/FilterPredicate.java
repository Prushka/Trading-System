package phase2.trade.view;

public interface FilterPredicate<T, E> {
    boolean test(T entity, E toMatch);
}
