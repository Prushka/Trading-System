package menu.data;


import java.util.HashMap;
import java.util.Map;

public class NodeRequest {

    Map<String, String> requestMap = new HashMap<>();

    public NodeRequest put(String key, String value) {
        requestMap.put(key, value);
        return this;
    }

    public String get(String key) {
        return requestMap.get(key);
    }

    public String toString() {
        return "Request: " + requestMap.toString();
    }

}
