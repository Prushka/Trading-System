package group.repository;

import group.repository.reflection.CSVMappable;
import group.repository.reflection.EntityMappingFactory;
import group.system.SaveHook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * The CSV implementation of storing and reading the list of entities to and from CSV files.
 *
 * @param <T> The entity this Repository deals with
 * @author Dan Lyu
 * @author lecture code, Logging project
 * @see RepositorySavable
 * @see RepositoryBase
 */
public class CSVRepository<T extends CSVMappable & UniqueId> extends RepositoryBase<T> {

    /**
     * The factory used to instantiate a mappable object using fixed parameter <code>List</code>.
     */
    private final EntityMappingFactory<T> factory;

    /**
     * Constructs a CSVRepository for saving and reading csv files.
     *
     * @param path     the file path
     * @param factory  the constructor reference for the mappable object
     * @param saveHook the repository will be saved by a saveHook
     */
    public CSVRepository(String path, EntityMappingFactory<T> factory, SaveHook saveHook) {
        super(path, saveHook);
        data = new ArrayList<>();
        this.factory = factory;
        if (file.exists()) {
            readUnsafe();
        }
    }

    /**
     * Reads the file. Doesn't check null or if file exists.
     * Uses {@link #factory} to instantiate specific objects.
     */
    private void readUnsafe() {
        // FileInputStream can be used for reading raw bytes, like an image.
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(file.getPath()));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "The file couldn't be found: " + file.getName(), e);
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

    /**
     * Saves the {@link #data} to the file, uses {@link #saveUnsafe} internally
     */
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
                saveUnsafe(entityNotNull);
            }
        }
    }

    /**
     * Saves {@link #data} to {@link #file} in CSV format.
     */
    private void saveUnsafe(T entityNotNull) {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(entityNotNull.toCSVHeader());
            for (T single : data) {
                if (data == null) {
                    writer.println(entityNotNull.toNullString());
                }
                writer.println(single.toCSVString());
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong when trying to save the file: " + file.getName(), e);
        }
    }
}
