package repository;

import java.util.List;

public interface EntityMappingFactory<T> {

    T get(List<String> data);
}
