package phase2.trade.config;

public class RedisConfig {

    private String host = "muddy.ca";

    private int port = 6380;

    private String password = "p8kgf3I!R77k";

    private int poolSize = 10;

    private int timeOut = 2000;

    private String channel = "group";

    private boolean useRedis = true;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public String getChannel() {
        return channel;
    }

    public boolean isUseRedis() {
        return useRedis;
    }
}
