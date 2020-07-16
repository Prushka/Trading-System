import group.repository.UniqueId;
import group.repository.reflection.CSVMappable;
import group.repository.reflection.MappableBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestEntity extends MappableBase implements CSVMappable, UniqueId {

    private Map<Long, String> testMap1;
    private Map<String, Boolean> testMap2;

    private Long uid;

    public TestEntity() {
        // testMap1 = new HashMap<>();
        // testMap1.put(23L, "aha");
        // testMap1.put(34L, "lll");
//
        // testMap2 = new HashMap<>();
        // testMap2.put("girl", true);
        // testMap2.put("boy", false);
    }

    public TestEntity(List<String> data) {
        super(data);
        System.out.println(testMap1.toString());
        System.out.println(testMap2.toString());
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
