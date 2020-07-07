package repository.map;

import java.lang.reflect.Field;
import java.util.Comparator;

public class FieldComparator implements Comparator<Field> {

    public int compare(Field f1, Field f2) {
        return f1.getName().compareTo(f2.getName());
    }

}