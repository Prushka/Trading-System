package group.repository.map;

import java.util.List;

public interface EntityMappingFactory<T> {

    T get(List<String> data);
}
