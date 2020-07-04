package repository;

import admin.Ticket;

import java.beans.IntrospectionException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SerializableRepository<T extends Serializable> implements Repository<T> {

    private List<T> data;

    private final String path;

    public SerializableRepository(String path) {
        data = new ArrayList<>();
        this.path = path;
        read();
    }

    @SuppressWarnings("unchecked")
    public void read(){ // this one is in interface, maybe also need to be private
        if(!new File(path).exists()){
            return;
        }
        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            data = (List<T>) input.readObject();
            input.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void save(){
        try {
            OutputStream file = new FileOutputStream(path);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);

            // serialize the
            output.writeObject(data);
            output.close();
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
