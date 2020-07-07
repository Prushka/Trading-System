package repository;

import repository.map.EntityMappable;
import repository.map.EntityMappingFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CSVRepository<T extends EntityMappable & UniqueId> extends RepositoryBase<T> {


    /**
     * The factory that is used to instantiate a mappable object.
     * It's a constructor reference of that class.
     */
    private final EntityMappingFactory<T> factory;

    /**
     * Construct a CSVRepository for saving and reading csv files with
     * the factory for instantiating mappable objects.
     *
     * @param path    the file path
     * @param factory the constructor reference for the mappable object
     */
    public CSVRepository(String path, EntityMappingFactory<T> factory) {
        super(path);
        data = new ArrayList<>();
        this.factory = factory;
        if (file.exists()) {
            readSafe();
        }
    }

    /**
     * Read the file with {@link #file} into the {@link #data}.
     * It will use the {@link #factory} to instantiate the specific objects.
     */
    @Override
    protected void readSafe() {
        // FileInputStream can be used for reading raw bytes, like an image.
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(file.getPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert scanner != null;
        scanner.nextLine(); // skip header
        // String[] headers = scanner.nextLine().split(",");
        String[] record;
        while (scanner.hasNextLine()) {
            record = scanner.nextLine().split(",");
            data.add(factory.get(Arrays.asList(record)));
        }
        scanner.close();
    }

    /**
     * Save {@link #data} to {@link #file}.
     */
    protected void saveSafe() {
        try {
            FileWriter writer = new FileWriter(file,false);
            // List<String> header = supplier.get().getHeader();

            // writer.append(String.join(",",header));
            // Iterator<Map<String,String>> iterator = dataBase.iterator();
            writer.append(get(0).toCSVHeader());
            for (T single : data) {
                writer.append(single.toCSVString()).append("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
