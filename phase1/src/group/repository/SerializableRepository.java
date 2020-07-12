package group.repository;

import group.system.SaveHook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Serialization implementation of storing and reading the list of entities to and from ser files.
 *
 * @param <T> the entity type to be used
 * @author Dan Lyu
 * @author lecture code, Logging project
 * @see RepositorySavable
 * @see RepositoryBase
 */
public class SerializableRepository<T extends Serializable & UniqueId> extends RepositoryBase<T> {

    /**
     * Constructs a Serialization Repository for saving and reading .ser files
     *
     * @param path the file path
     */
    public SerializableRepository(String path, SaveHook saveHook) {
        super(path, saveHook);
        data = new ArrayList<>();
        if (file.exists()) {
            readUnsafe();
        }
    }

    /**
     * Reads the file. Doesn't check null or if file exists.
     * Reads the file from {@link #file}.
     */
    @SuppressWarnings("unchecked")
    private void readUnsafe() {
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
     * Saves the {@link #data} to the file, uses {@link #saveUnsafe} internally
     */
    public void save() {
        if (data != null && data.size() > 0) {
            saveUnsafe();
        }
    }

    /**
     * Saves {@link #data} to {@link #file} in serialization format.
     */
    private void saveUnsafe() {
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
