package phase2.trade.editor;

@FunctionalInterface
public interface EntityIdLookUp<T> {
    Long getId(T entity);
}
