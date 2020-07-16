package group.menu.data;

import java.util.HashMap;
import java.util.Map;

/**
 * The persistent request class to hold cached user requests.<p>
 * This class is currently not used since current logged in User is cached elsewhere.
 *
 * @author Dan Lyu
 * @see Request
 */
public class PersistentRequest {

    /**
     * A map of persistent request key to the request object
     */
    private final Map<String, Request> cachedRequest = new HashMap<>();

    /**
     * Adds a standard request to the persistent area.
     *
     * @param key     the persistent request key
     * @param request the request to be cached
     */
    public void addCachedRequest(String key, Request request) {
        cachedRequest.put(key, request);
    }

    /**
     * Gets a standard persistent request from the key.
     *
     * @param key the key to lookup
     * @return the request object
     */
    public Request getCachedRequest(String key) {
        return cachedRequest.get(key);
    }

    /**
     * Clears all persistent request.
     */
    public void clear() {
        cachedRequest.clear();
    }

}
