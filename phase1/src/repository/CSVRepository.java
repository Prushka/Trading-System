package repository;

import java.io.*;
import java.util.*;
import java.util.function.Supplier;

public class CSVRepository<T extends Mappable> implements Repository<T> {

    private final List<T> data;

    private final String path;

    private final MappingFactory<T> factory;

    public CSVRepository(String path, MappingFactory<T> factory) {
        data = new ArrayList<>();
        this.path = path;
        this.factory = factory;
        read();
    }

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
}
