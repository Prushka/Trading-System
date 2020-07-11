package group.repository;

import group.repository.map.EntityMappable;
import group.repository.map.EntityMappingFactory;
import group.system.SaveHook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The CSV Repository Implementation for storing and reading the list of entities
 *
 * @param <T> The entity this Repository deals with
 * @author Dan Lyu
 * @author lecture code, Logging project
 * @see Repository
 * @see RepositoryBase
 */
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
    public CSVRepository(String path, EntityMappingFactory<T> factory, SaveHook saveHook) {
        super(path, saveHook);
        data = new ArrayList<>();
        this.factory = factory;
        if (file.exists()) {
            readSafe();
        }
    }

    /**
     * Read the file with {@link #file} into {@link #data}.
     * It will use the {@link #factory} to instantiate the specific objects.
     */
    void readSafe() {
        // FileInputStream can be used for reading raw bytes, like an image.
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(file.getPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert scanner != null;
        scanner.nextLine(); // skip header
        String[] record;
        while (scanner.hasNextLine()) {
            String recordString = scanner.nextLine();
            String testString = recordString.replaceAll("[, ]", "");
            if (testString.length() == 0) {
                data.add(null);
                continue;
            }
            record = recordString.split(",");
            data.add(factory.get(Arrays.asList(record)));
        }
        scanner.close();
    }

    @Override
    public void save() {
        if (data != null && data.size() > 0) {
            T entityNotNull = null;
            for (T single : data) {
                if (single != null) {
                    entityNotNull = single;
                    break;
                }
            }
            if (entityNotNull != null) {
                saveSafe(entityNotNull);
            }
        }
    }

    /**
     * Save {@link #data} to {@link #file}.
     */
    void saveSafe(T entityNotNull) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.append(entityNotNull.toCSVHeader());
            for (T single : data) {
                if (data == null) {
                    writer.println(entityNotNull.toNullString());
                }
                writer.println(single.toCSVString());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
