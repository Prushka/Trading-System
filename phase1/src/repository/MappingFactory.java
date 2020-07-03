package repository;

import java.util.List;

public interface MappingFactory<T> {

    T get(List<String> data);
}
