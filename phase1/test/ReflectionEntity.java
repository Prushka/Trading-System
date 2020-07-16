import group.repository.UniqueId;
import group.repository.reflection.CSVMappable;
import group.repository.reflection.MappableBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionEntity extends MappableBase implements CSVMappable, UniqueId {

    private Map<Long, Long> testMap;

    private Long uid;

    public ReflectionEntity() {
        // testMap = new HashMap<>();
        // testMap.put(23L, 12L);
        // testMap.put(34L, 123L);
    }

    public ReflectionEntity(List<String> data) {
        super(data);
    }

    @Override
    public void setUid(long value) {
        this.uid = value;
    }

    @Override
    public long getUid() {
        return uid;
    }
}
