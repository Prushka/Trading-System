package repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataBaseIterator implements Iterator<Map<String, String>> {

    private final List<Map<String, String>> data;

    private int pointer;

    public DataBaseIterator(List<Map<String, String>> data) {
        this.data = data;
        this.pointer = -1;
    }

    @Override
    public boolean hasNext() {
        return data.size() > pointer;
    }

    @Override
    public Map<String, String> next() {
        pointer += 1;
        if (hasNext()) {
            return data.get(pointer);
        }
        return null;
    }
}
