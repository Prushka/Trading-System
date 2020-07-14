package group.menu.data;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Request object that has a map of
 * {@link group.menu.node.RequestableNode#getKey()} -> user input ({@link group.menu.node.RequestableNode#getValue()}).<p>
 * <p>
 * The Request is constructed automatically in {@link group.menu.node.SubmitNode}
 *
 * @author Dan Lyu
 */
public class Request {

    /**
     * The Request Map to hold the key and value in order
     */
    Map<String, String> requestMap = new LinkedHashMap<>();

    /**
     * The timestamp when the Request is made
     */
    private long timeStamp;

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

    /**
     * @param key the key to lookup
     * @return the value in Long
     */
    public Long getLong(String key) {
        return Long.valueOf(get(key));
    }

    /**
     * @param key the key to lookup
     * @return the value in Int
     */
    public Integer getInt(String key) {
        return Integer.valueOf(get(key));
    }

    /**
     * @param key the key to lookup
     * @return the value in Boolean
     */
    public Boolean getBoolean(String key) {
        return Boolean.valueOf(get(key));
    }

    /**
     * @param key the key to lookup
     * @return the value in Double
     */
    public Double getDouble(String key) {
        return Double.valueOf(get(key));
    }

    /**
     * @param key the key to lookup
     * @return the value in Float
     */
    public Float getFloat(String key) {
        return Float.valueOf(get(key));
    }

    /**
     * Use only when the value is in standard timestamp (ms) format
     *
     * @param key the key to lookup
     * @return the value in Date
     */
    public Date getDate(String key) {
        return new Date(getLong(key));
    }

    /**
     * @return the timestamp the Request is made
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the {@link #timeStamp} to be the current time
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

    /**
     * @param persistentRequest add the global PersistentRequest object to the current Request
     */
    public void setPersistentRequest(PersistentRequest persistentRequest) {
        this.persistentRequest = persistentRequest;
    }

    /**
     * @return the global persistent request
     */
    public PersistentRequest getPersistentRequest() {
        return persistentRequest;
    }
}
