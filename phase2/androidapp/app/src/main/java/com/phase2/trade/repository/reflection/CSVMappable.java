package main.java.com.phase2.trade.repository.reflection;

import main.java.com.phase2.trade.repository.CSVRepository;

/**
 * Implement this interface to allow the class to be used in the {@link CSVRepository}.<p>
 * The reflection implementation of this interface: {@link MappableBase}.<p>
 * The reflection implementation is not required to be used since {@link CSVRepository} depends on this interface.
 *
 * @author Dan Lyu
 * @see MappableBase
 * @see CSVRepository
 */
public interface CSVMappable {

    /**
     * @return the csv String representation of the current class's fields' values
     */
    String toCSVString();

    /**
     * Returns the CSV header of all non-transient fields.
     * The value should include name of field and type in the implementation.
     *
     * @return the header String of the current class's fields' names and types
     */
    String toCSVHeader();

    /**
     * @return the String representation of an empty record, contains only commas
     */
    String toNullString();

}
