package phase2.trade.editor;

import java.util.List;

@FunctionalInterface
public interface EditorSupplier<E, T> {
    E get(List<T> entities);
}
