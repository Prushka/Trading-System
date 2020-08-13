package phase2.trade.controller;

@FunctionalInterface
public interface EntityIdLookUp<T> {
    Long getId(T entity);
}
