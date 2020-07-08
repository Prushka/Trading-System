package group.repository.map;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * The implementation of converting fields to CSV record. Has a constructor
 * that takes in CSV record using reflection.
 *
 * @author Dan Lyu
 * @author "instantiating an enum using reflection" by Bozho
 *
 * @see EntityMappable
 * @see <a href="https://stackoverflow.com/questions/3735927/java-instantiating-an-enum-using-reflection">Instantiating an enum using reflection</a>
 * @see <a href="https://stackoverflow.com/questions/10638826/java-reflection-impact-of-setaccessibletrue">Impact of setAccessible(true)</a>
 */
public abstract class MappableBase {

    /**
     * The empty constructor used to make sure that extending
     * this class will have no impact on constructors of subclasses.
     */
    public MappableBase() {}

    /**
     * This constructor will construct the object itself using
     * fields and their corresponding String representation
     *
     * @param data the CSV record split into list
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
                }
                field.set(this, obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            id++;
        }
    }

    /**
     * Returns the field name and type in order.
     *
     * @return the Header String record for this entity
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
     * @return the sorted fields of the current object
     *
     * @see <a href="https://stackoverflow.com/questions/1097807/java-reflection-is-the-order-of-class-fields-and-methods-standardized">Field Order in Reflection</a>
     * @see FieldComparator
     */
    private List<Field> getSortedFields() {
        List<Field> fields = Arrays.asList(this.getClass().getDeclaredFields());
        fields.sort(new FieldComparator());
        return fields;
    }

    /**
     * @return the String record representation of all fields in current class
     */
    public String toCSVString() {
        StringBuilder value = new StringBuilder();
        for (Field field : getSortedFields()) {
            try {
                if (!Modifier.isTransient(field.getModifiers())) {

                    field.setAccessible(true); // how this makes private members accessible???
                    Object obj = field.get(this);
                    if (obj == null) {
                        value.append("null");
                    } else if (obj instanceof Enum) {
                        value.append(((Enum<?>) obj).name());
                    } else {
                        value.append(obj.toString());
                    }
                    value.append(",");
                }
            } catch (IllegalAccessException | NullPointerException e) {
                //TODO: implement better error handling
                value.append("null");
                e.printStackTrace();
            }
        }
        return value.substring(0, value.length() - 1);
    }

}
