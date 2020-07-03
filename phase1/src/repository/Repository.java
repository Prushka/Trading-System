package repository;

import java.util.List;

public interface Repository<T> {

    void save();
    void read();

    void add(T data);
    T get(int id);

}
