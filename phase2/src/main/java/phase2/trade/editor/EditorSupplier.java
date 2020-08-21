package phase2.trade.editor;

import phase2.trade.config.ConfigBundle;

import java.util.List;

/**
 * The functional interface Editor supplier.
 *
 * @param <E> the entity editor type
 * @param <T> the entity type
 * @author Dan Lyu
 */
@FunctionalInterface
public interface EditorSupplier<E extends Editor<T>, T> {
    /**
     * Gets the editor.
     *
     * @param entities     the entities
     * @param configBundle the config bundle
     * @return the e
     */
    E get(List<T> entities, ConfigBundle configBundle);
}
