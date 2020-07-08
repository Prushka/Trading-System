package group.menu.data;


import java.util.LinkedHashMap;
import java.util.Map;

public class Request {

    Map<String, String> requestMap = new LinkedHashMap<>();

    private long timeStamp;

    // TODO: Attach current user information if exists one

    public Request put(String key, String value) {
        requestMap.put(key, value);
        return this;
    }

    public String get(String key) {
        return requestMap.get(key);
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp() {
        this.timeStamp = System.currentTimeMillis() / 1000L;
    }

    @Override
    public String toString() {
        return "Request: " + requestMap.toString();
    }

}
