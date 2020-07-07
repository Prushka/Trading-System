package repository.map;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public abstract class MappableBase {

    public MappableBase(){

    }

    // is it necessary to put this part into use case?

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

    public String toCSVHeader() {
        StringBuilder value = new StringBuilder();
        getSortedFields().forEach(field -> value.append(field.getName()).append("(")
                .append(field.getType().getSimpleName()).append(")").append(","));
        value.setLength(value.length() - 1);
        value.append("\n");
        return value.toString();
    }

    private List<Field> getSortedFields(){
        List<Field> fields = Arrays.asList(this.getClass().getDeclaredFields());
        fields.sort(new FieldComparator());
        return fields;
    }

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
        System.out.println(value.substring(0, value.length() - 1));
        return value.substring(0, value.length() - 1);
    }

}
