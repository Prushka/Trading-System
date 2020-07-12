package group.menu.data;

import java.util.HashMap;
import java.util.Map;

public class PersistentRequest {

    private final Map<String, Request> cachedRequest = new HashMap<>();

    public void addCachedRequest(String key, Request request) {
        cachedRequest.put(key, request);
    }

    public Request getCachedRequest(String key) {
        return cachedRequest.get(key);
    }

    public void clear() {
        cachedRequest.clear();
    }

}
