package phase2.trade.editor;

import phase2.trade.config.ConfigBundle;

import java.util.List;

@FunctionalInterface
public interface EditorSupplier<E, T> {
    E get(List<T> entities, ConfigBundle configBundle);
}
