package group.repository.reflection;

import java.util.List;

/**
 * The functional interface used to take in <code>List<String></code> representation
 * and construct the entity itself.<p>
 * Used as a constructor reference.
 *
 * @param <T> The entity to be mapped
 * @author Dan Lyu
 */

@FunctionalInterface
public interface EntityMappingFactory<T> {

    /**
     * Constructs the entity.
     *
     * @param data the CSV representation List
     * @return the entity object
     */
    T get(List<String> data);
}
