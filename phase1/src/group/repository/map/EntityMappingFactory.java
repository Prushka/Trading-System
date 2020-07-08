package group.repository.map;

import java.util.List;

/**
 * The constructor reference used to take in List<String> representation
 * and construct the entity itself.
 *
 * @param <T> The entity that needs to be mapped
 *
 * @author Dan Lyu
 */

@FunctionalInterface
public interface EntityMappingFactory<T> {

    /**
     * It will use the constructor reference of the entity
     *
     * @param data the CSV record
     * @return the entity object
     */
    T get(List<String> data);
}
