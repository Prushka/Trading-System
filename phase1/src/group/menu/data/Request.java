package group.menu.data;

import group.menu.node.InputNode;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Request object that has a map of
 * {@link InputNode#getKey()} -> user input ({@link InputNode#getValue()}).<p>
 * <p>
 * The Request is constructed automatically in {@link group.menu.node.SubmitNode}
 *
 * @author Dan Lyu
 */
public class Request {

    /**
     * The Request Map has holds the key and value in order.
     */
    Map<String, String> requestMap = new LinkedHashMap<>();

    /**
     * The timestamp when the Request is made
     */
    private long timeStamp;

    // TODO: Attach current user information if exists one

    private PersistentRequest persistentRequest;

    /**
     * @param key   the key
     * @param value the user input
     * @return this Request object itself
     */
    public Request put(String key, String value) {
        requestMap.put(key, value);
        return this;
    }

    /**
     * @param key the key to lookup
     * @return the value in String
     */
    public String get(String key) {
        return requestMap.get(key);
    }

    public Long getLong(String key) {
        return Long.valueOf(get(key));
    }

    public Integer getInt(String key) {
        return Integer.valueOf(get(key));
    }

    public Boolean getBoolean(String key) {
        return Boolean.valueOf(get(key));
    }

    public Double getDouble(String key) {
        return Double.valueOf(get(key));
    }

    public Float getFloat(String key) {
        return Float.valueOf(get(key));
    }

    public Date getDate(String key) { // only use standard timestamp to ms here
        return new Date(getLong(key));
    }

    /**
     * @return {@link #timeStamp}
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * set the {@link #timeStamp} to be the current time
     */
    public void setTimeStamp() {
        this.timeStamp = System.currentTimeMillis();
    }

    /**
     * @return the String representation of this Request object
     */
    @Override
    public String toString() {
        return "Request: " + requestMap.toString();
    }

    public void setPersistentRequest(PersistentRequest persistentRequest) {
        this.persistentRequest = persistentRequest;
    }
}
