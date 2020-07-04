package repository;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CSVRepository<T extends Mappable> implements Repository<T> {

    /**
     * The list where entities are held.
     */
    private final List<T> data;

    /**
     * The path of this file
     */
    private final String path;

    /**
     * The factory that is used to instantiate a mappable object.
     * It's a constructor reference of that object.
     */
    private final MappingFactory<T> factory;

    /**
     * Construct a CSVRepository for saving and reading csv files with
     * the factory for instantiating mappable objects.
     *
     * @param path the file path
     * @param factory the constructor reference for the mappable object
     */
    public CSVRepository(String path, MappingFactory<T> factory) {
        data = new ArrayList<>();
        this.path = path;
        this.factory = factory;
        read();
    }

    private void bean(T data) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        for(PropertyDescriptor descriptor:Introspector.getBeanInfo(
                data.getClass(), Object.class)
                .getPropertyDescriptors()){
            System.out.println(descriptor.getReadMethod().invoke(data));
        }
    }

    /**
     * Read the file with {@link #path} into the {@link #data}.
     * It will use the {@link #factory} to instantiate the specific objects.
     */
    @Override
    public void read() {
        // FileInputStream can be used for reading raw bytes, like an image.
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert scanner != null;
        // String[] headers = scanner.nextLine().split(",");
        String[] record;
        while (scanner.hasNextLine()) {
            record = scanner.nextLine().split(",");
            data.add(factory.get(Arrays.asList(record)));
        }
        scanner.close();
    }

    /**
     * Save {@link #data} to {@link #path}.
     */
    public void save() {
        try {
            FileWriter writer = new FileWriter(path);
            // List<String> header = supplier.get().getHeader();

            // writer.append(String.join(",",header));
            // Iterator<Map<String,String>> iterator = dataBase.iterator();
            for (T single : data) {
                writer.append(String.join(",", single.toList()));
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(T data) {
        this.data.add(data);
    }

    public T get(int id) {
        return data.get(id);
    }

    @Override
    public Iterator<T> iterator() {
        return new RepositoryIterator<>(data);
    }
}
