package com.phase2.trade.repository.reflection;

/**
 * Implement this interface to allow the class to be used in the {@link com.phase2.trade.repository.CSVRepository}.<p>
 * The reflection implementation of this interface: {@link MappableBase}.<p>
 * The reflection implementation is not required to be used since {@link com.phase2.trade.repository.CSVRepository} depends on this interface.
 *
 * @author Dan Lyu
 * @see MappableBase
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
