package group.repository.reflection;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * <p>A comparator to compare fields in alphabetic order.</p>
 *
 * </p>We didn't come up with the original idea. Please see the reference link for details.</p>
 *
 * @see <a href="https://stackoverflow.com/questions/1097807/java-reflection-is-the-order-of-class-fields-and-methods-standardized">Field Order in Reflection</a>
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html">Class Documentation</a>
 */
public class FieldComparator implements Comparator<Field> {

    /**
     * Compare the fields object
     *
     * @param f1 the first field to be compared
     * @param f2 the second field to be compared
     * @return the String {@link String#compareTo(String)} result
     */
    public int compare(Field f1, Field f2) {
        return f1.getName().compareTo(f2.getName());
    }

}