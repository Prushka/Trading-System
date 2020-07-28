package main.java.com.phase2.trade.repository;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import group.system.SaveHook;

/**
 * The Serialization implementation of storing and reading the list of entities to and from ser files.
 *
 * @param <T> the entity type to be used
 * @author Dan Lyu
 * @author lecture code, Logging project
 * @see RepositorySaveImpl
 */
public class SerializableRepository<T extends Serializable & UniqueId> extends RepositorySaveImpl<T> {

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
            LOGGER.log(Level.SEVERE, "Something went wrong when trying to read: " + file.getName(), e);
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
            LOGGER.log(Level.SEVERE, "Something went wrong when trying to save: " + file.getName(), e);
        }
    }

}
