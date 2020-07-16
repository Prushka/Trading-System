package group.repository.reflection;

import group.config.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The reflection implementation of {@link CSVMappable}.<p>
 * Contains a constructor that takes in a CSV representation String.<p>
 * Subclasses are required to have a sub-constructor corresponding to <code>MappableBase(List data)</code><p>
 * This reflection implementation has limits on certain classes,
 * use only non-final List, Integer, Long, Float, Double, Boolean, String, Date, Enum and CSVMappable fields in the entity class if you extend this class.<p>
 * To allow more more flexibility, implement {@link CSVMappable} directly in the entity class.
 *
 * @author Dan Lyu
 * @author Bozho - <a href="https://stackoverflow.com/questions/3735927/java-instantiating-an-enum-using-reflection">Instantiating an enum using reflection</a>
 * @author BalusC - <a href="https://stackoverflow.com/questions/1942644/get-generic-type-of-java-util-list">Get generic type of java.util.List</a>
 * @see CSVMappable
 */

@SuppressWarnings("unchecked")
public abstract class MappableBase {

    static transient final Logger LOGGER = new LoggerFactory(MappableBase.class).getConfiguredLogger();

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
        System.out.println(data.size());
        for (Field field : getSortedFields()) {
            field.setAccessible(true);
            String representation = data.get(id);
            Object obj;
            try {
                // what's the difference between == and isAssignedFrom() and isAssignableFrom() from the class?
                if (CSVMappable.class.isAssignableFrom(field.getType())) { // aha
                    List<String> childRepresentation = new ArrayList<>();
                    for (Field ignored : getSortedFields(field.getType())) {
                        childRepresentation.add(data.get(id));
                        id += 1;
                    }
                    obj = field.getType().getDeclaredConstructor(List.class).newInstance(childRepresentation);
                    id -= 1;
                } else if (List.class.isAssignableFrom(field.getType())) {
                    if (representation.equals("null")) {
                        obj = null;
                    } else {
                        obj = stringToList((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0], representation);
                    }
                } else if (Map.class.isAssignableFrom(field.getType())) {
                    if (representation.equals("null")) {
                        obj = null;
                    } else {
                        obj = stringToMap((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]
                                , (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1], representation);
                    }
                } else {
                    obj = getObjectFrom(field.getType(), representation);
                }
                field.set(this, obj);
                id++;
            } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                LOGGER.log(Level.SEVERE, "Some error occurred in reflection when instantiating an object from CSV record.", e);
            }
        }
    }

    private <K, V> Map<K, V> stringToMap(Class<K> keyClass, Class<V> valueClass, String representation) { // this only uses HashMap
        Map<K, V> map = new HashMap<>();
        if (representation.length() == 0) return map;
        for (String element : representation.split(";", -1)) {
            String[] subElement = element.split(":", -1);
            map.put((K) getObjectFrom(keyClass, subElement[0]), (V) getObjectFrom(valueClass, subElement[1]));
        }
        return map;
    }

    /**
     * @param fieldTypeClass the field type
     * @param representation the String representation
     * @return the object of the field type
     */
    private Object getObjectFrom(Class<?> fieldTypeClass, String representation) {
        Object obj;
        if (representation.equals("null")) {
            obj = null; // this is necessary since classes below can be null
        } else if (fieldTypeClass.isEnum()) {
            obj = Enum.valueOf((Class<Enum>) fieldTypeClass, representation);
        } else if (Long.class.isAssignableFrom(fieldTypeClass)) {
            obj = Long.valueOf(representation);
        } else if (Integer.class.isAssignableFrom(fieldTypeClass)) {
            obj = Integer.valueOf(representation);
        } else if (Boolean.class.isAssignableFrom(fieldTypeClass)) {
            obj = Boolean.valueOf(representation);
        } else if (Double.class.isAssignableFrom(fieldTypeClass)) {
            obj = Double.valueOf(representation);
        } else if (Float.class.isAssignableFrom(fieldTypeClass)) {
            obj = Float.valueOf(representation);
        } else if (String.class.isAssignableFrom(fieldTypeClass)) {
            obj = representation;
        } else if (Date.class.isAssignableFrom(fieldTypeClass)) {
            obj = new Date(Integer.parseInt(representation));
        } else {
            obj = null;
        }
        return obj;
    }

    /**
     * @param fieldListGenericClass the field type of the elements in the List
     * @param representation        the String representation
     * @param <T>                   the generic type to be used in the list
     * @return the List<T> generated from the String representation
     */
    private <T> List<T> stringToList(Class<T> fieldListGenericClass, String representation) {
        List<T> list = new ArrayList<>();
        if (representation.length() == 0) return list;
        for (String element : representation.split(";")) {
            list.add((T) getObjectFrom(fieldListGenericClass, element));
        }
        return list;
    }

    /**
     * @return the String representation of current class's non-transient fields
     */
    public String toCSVHeader() {
        return toCSVHeader(this.getClass(), false);
    }

    /**
     * Returns the representation of the class's field name and type in alphabetic order.
     *
     * @param clazz  the class to exam for fields
     * @param prefix the prefix of the header element
     * @return the String representation of the class's non-transient fields
     */
    public String toCSVHeader(Class<?> clazz, Boolean prefix) {
        StringBuilder value = new StringBuilder();
        for (Field field : getSortedFields(clazz)) {
            if (CSVMappable.class.isAssignableFrom(field.getType())) {
                value.append(toCSVHeader(field.getType(), true)).append(",");
            } else {
                if (prefix) value.append(clazz.getSimpleName()).append(".");
                value.append(field.getName()).append("(")
                        .append(field.getType().getSimpleName()).append(")").append(",");
            }
        }
        value.setLength(value.length() - 1);
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
     * @param clazz the class to exam for fields
     * @return the sorted and non-transient fields of the class
     */
    private List<Field> getSortedFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (!currentClass.getName().equals(Object.class.getName())) {
            System.out.println(currentClass.getName());
            fields.addAll(Arrays
                    .stream(currentClass.getDeclaredFields())
                    .filter(this::ifFieldNotTransient)
                    .sorted(new FieldComparator())
                    .collect(Collectors.toList()));
            currentClass = currentClass.getSuperclass();
        }
        return fields;
    }

    /**
     * @return the sorted and non-transient fields of the current class
     */
    private List<Field> getSortedFields() {
        return getSortedFields(this.getClass());
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
                } else if (obj instanceof CSVMappable) {
                    value.append(((CSVMappable) obj).toCSVString());
                } else if (obj instanceof List) {
                    ((List<?>) obj).forEach(o -> value.append(o.toString()).append(";"));
                } else if (obj instanceof Map) {
                    for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                        value.append(entry.getKey().toString()).append(":").append(entry.getValue().toString()).append(";");
                    }
                } else {
                    value.append(obj.toString());
                }
                value.append(",");
            } catch (IllegalAccessException | NullPointerException e) {
                value.append("null");
                LOGGER.log(Level.WARNING, "Field " + field.getName() + " cannot be mapped to a value!", e);
            }
        }
        return value.substring(0, value.length() - 1);
    }

}
