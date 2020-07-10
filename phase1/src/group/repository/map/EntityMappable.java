package group.repository.map;

/**
 * Implement this interface to allow the entity to be used in the {@link group.repository.CSVRepository}
 * The methods' implementation are present in the {@link MappableBase}
 * The simple way to allow an entity to be mapped to a CSV file is to let it extend
 * {@link MappableBase} and implement {@link EntityMappable}
 *
 * @author Dan Lyu
 * @see MappableBase
 * @see group.repository.CSVRepository
 */
public interface EntityMappable {

    /**
     * @return the csv record String of the current entity
     */
    String toCSVString();

    /**
     * The value should include name of field and type in the implementation.
     *
     * @return the header record String of the current entity
     */
    String toCSVHeader();

    String toNullString();

}
