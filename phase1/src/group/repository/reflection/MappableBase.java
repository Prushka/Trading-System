package group.repository.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The reflection implementation of {@link CSVMappable}.<p>
 * Contains a constructor that takes in a CSV representation String.<p>
 * Subclasses are required to have a sub-constructor corresponding to <code>MappableBase(List data)</code><p>
 * This reflection implementation has limits on certain classes.<p>
 * Use only non-final Long, Integer, Float, Double, Boolean, String, Date and Enum fields in the entity class if you extend this class.<p>
 * To allow more flexibility, implement {@link CSVMappable} directly in the entity class.
 *
 * @author Dan Lyu
 * @author Bozho - "instantiating an enum using reflection"
 * @see CSVMappable
 * @see <a href="https://stackoverflow.com/questions/3735927/java-instantiating-an-enum-using-reflection">Instantiating an enum using reflection</a>
 * @see <a href="https://stackoverflow.com/questions/10638826/java-reflection-impact-of-setaccessibletrue">Impact of setAccessible(true)</a>
 */
public abstract class MappableBase {

    /**
     * The empty constructor used to make sure that extending
     * this class will have no impact on constructors of subclasses.
     */
    public MappableBase() {
    }

    /**
     * Constructs the object itself using
     * fields and their corresponding String representation.
     *
     * @param data the CSV representation String List
     */
    public MappableBase(List<String> data) {
        int id = 0;
        for (Field field : getSortedFields()) {
            field.setAccessible(true);
            String representation = data.get(id);
            Object obj = null; // declaring within the smallest scope. https://stackoverflow.com/questions/8803674/declaring-variables-inside-or-outside-of-a-loop
            try {
                // what's the difference between == and isAssignedFrom() and isAssignableFrom() from the class?
                if (representation.equals("null")) {
                    obj = null;
                } else if (field.getType().isEnum()) {
                    obj = Enum.valueOf((Class<Enum>) field.getType(), representation);
                } else if (Integer.class.isAssignableFrom(field.getType())) {
                    obj = Integer.parseInt(representation);
                } else if (Long.class.isAssignableFrom(field.getType())) {
                    obj = Long.parseLong(representation);
                } else if (Boolean.class.isAssignableFrom(field.getType())) {
                    obj = Boolean.parseBoolean(representation);
                } else if (Double.class.isAssignableFrom(field.getType())) {
                    obj = Double.parseDouble(representation);
                } else if (Float.class.isAssignableFrom(field.getType())) {
                    obj = Float.parseFloat(representation);
                } else if (String.class.isAssignableFrom(field.getType())) {
                    obj = representation;
                } else if (Date.class.isAssignableFrom(field.getType())) {
                    obj = new Date(Long.parseLong(representation));
                }
                field.set(this, obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            id++;
        }
    }

    /**
     * Returns the field name and type in alphabetic order.
     *
     * @return the String representation of current class's non-transient fields
     */
    public String toCSVHeader() {
        StringBuilder value = new StringBuilder();
        getSortedFields().forEach(field -> value.append(field.getName()).append("(")
                .append(field.getType().getSimpleName()).append(")").append(","));
        value.setLength(value.length() - 1);
        value.append("\n");
        return value.toString();
    }

    /**
     * @param field the field to be checked
     * @return <code>true</code> if the field is not transient
     */
    private boolean ifFieldNotTransient(Field field) {
        return !Modifier.isTransient(field.getModifiers());
    }

    /**
     * @return the sorted and non-transient fields of the current object
     * @see <a href="https://stackoverflow.com/questions/1097807/java-reflection-is-the-order-of-class-fields-and-methods-standardized">Field Order in Reflection</a>
     * @see FieldComparator
     */
    private List<Field> getSortedFields() {
        return Arrays
                .stream(this.getClass().getDeclaredFields())
                .filter(this::ifFieldNotTransient)
                .sorted(new FieldComparator())
                .collect(Collectors.toList());
    }

    /**
     * @return the String representation of current class's field's empty values, contains only commas
     */
    public String toNullString() {
        StringBuilder sb = new StringBuilder();
        getSortedFields().forEach(field -> sb.append(","));
        return sb.toString();
    }

    /**
     * Returns the String representation in alphabetic order of field's names
     *
     * @return the String representation of non-transient fields's values in current class
     */
    public String toCSVString() {
        StringBuilder value = new StringBuilder();
        for (Field field : getSortedFields()) {
            try {
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj == null) {
                    value.append("null");
                } else if (obj instanceof Enum) {
                    value.append(((Enum<?>) obj).name());
                } else if (obj instanceof Date) {
                    value.append(((Date) obj).getTime());
                } else {
                    value.append(obj.toString());
                }
                value.append(",");
            } catch (IllegalAccessException | NullPointerException e) {
                //TODO: implement better error handling
                value.append("null");
                e.printStackTrace();
            }
        }
        return value.substring(0, value.length() - 1);
    }

}
