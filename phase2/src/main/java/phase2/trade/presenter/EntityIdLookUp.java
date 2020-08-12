package phase2.trade.presenter;

@FunctionalInterface
public interface EntityIdLookUp<T> {
    Long getId(T entity);
}
