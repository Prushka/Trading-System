package phase2.trade.config;

/**
 * The type Redis config.
 *
 * @author Dan Lyu
 */
public class RedisConfig {

    private final String host = "muddy.ca";

    private final int port = 6380;

    private final String password = "p8kgf3I!R77k";

    private final int poolSize = 10;

    private final int timeOut = 2000;

    private final String channel = "group";

    private final boolean useRedis = true;

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets pool size.
     *
     * @return the pool size
     */
    public int getPoolSize() {
        return poolSize;
    }

    /**
     * Gets time out.
     *
     * @return the time out
     */
    public int getTimeOut() {
        return timeOut;
    }

    /**
     * Gets channel.
     *
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * If this is set to false in configuration. Redis and its pub-sub won't be used.
     *
     * @return the boolean
     */
    public boolean isUseRedis() {
        return useRedis;
    }
}
