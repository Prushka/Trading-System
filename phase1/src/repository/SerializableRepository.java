package repository;

import java.io.*;
import java.util.*;

public class SerializableRepository<T extends Serializable & UniqueId> extends RepositoryBase<T> {
    // should we have a unique id on every entity?

    public SerializableRepository(String path) {
        super(path);
        data = new ArrayList<>();
        if(file.exists()){
            readSafe();
        }
    }

    @SuppressWarnings("unchecked")
    protected void readSafe() {
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

    protected void saveSafe() {
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
