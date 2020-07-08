package group.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Serialization Implementation for storing and reading the list of entities
 *
 * @param <T> The entity this Repository deals with
 *
 * @author Dan Lyu
 * @author lecture code, Logging project
 * @see Repository
 * @see RepositoryBase
 */
public class SerializableRepository<T extends Serializable & UniqueId> extends RepositoryBase<T> {

    /**
     * Construct a CSVRepository for saving and reading .ser files
     *
     * @param path the file path
     */
    public SerializableRepository(String path) {
        super(path);
        data = new ArrayList<>();
        if(file.exists()){
            readSafe();
        }
    }

    /**
     * Read the file with {@link #file} into {@link #data}.
     */
    @SuppressWarnings("unchecked")
    void readSafe() {
        try {
            InputStream inputStream = new FileInputStream(file.getPath());
            InputStream buffer = new BufferedInputStream(inputStream);
            ObjectInput input = new ObjectInputStream(buffer);

            data = (List<T>) input.readObject();
            input.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save {@link #data} to {@link #file}.
     */
    void saveSafe() {
        try {
            OutputStream outputStream = new FileOutputStream(file.getPath());
            OutputStream buffer = new BufferedOutputStream(outputStream);
            ObjectOutput output = new ObjectOutputStream(buffer);

            // serialize the
            output.writeObject(data);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
