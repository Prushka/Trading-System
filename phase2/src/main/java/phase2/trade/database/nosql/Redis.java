package phase2.trade.database.nosql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.config.RedisConfig;
import phase2.trade.gateway.GatewayPubSub;
import phase2.trade.refresh.Reloadable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Redis implementation of {@link GatewayPubSub}.
 *
 * @author Dan Lyu
 */
public class Redis implements GatewayPubSub {

    private static final Logger logger = LogManager.getLogger(Redis.class);

    private JedisPool jedisPool;

    private final RedisConfig redisConfig;

    private Map<String, Reloadable> registeredControllers;

    private ExecutorService threadPool;

    private JedisPubSub subscriber;

    /**
     * Constructs a new Redis from the configuration and an injected map of registered controllers (used to reload controllers when other subscribers publish them).
     *
     * @param redisConfig           the redis config
     * @param registeredControllers the registered controllers
     */
    public Redis(RedisConfig redisConfig, Map<String, Reloadable> registeredControllers) {

        this.redisConfig = redisConfig;
        if (!redisConfig.isUseRedis()) return;

        this.registeredControllers = registeredControllers;
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisConfig.getPoolSize());

        this.jedisPool = new JedisPool(
                jedisPoolConfig,
                redisConfig.getHost(),
                redisConfig.getPort(),
                redisConfig.getTimeOut(),
                redisConfig.getPassword());

        threadPool = Executors.newFixedThreadPool(redisConfig.getPoolSize());

        subscribe();
    }

    private Jedis getConnection() {
        return jedisPool.getResource();
    }

    public void subscribe() {
        if (!redisConfig.isUseRedis()) return;
        subscribe(redisConfig.getChannel());
    }

    private void subscribe(String channel) {
        subscriber = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                logger.info(message);
                for (String clazzSimple : message.split(",", -1)) {
                    if (registeredControllers.containsKey(clazzSimple)) {
                        registeredControllers.get(clazzSimple).reload();
                    }
                }
            }
        };
        threadPool.submit(() -> {
            if (getConnection().isConnected())
                getConnection().subscribe(subscriber, channel);
        });
    }

    public void publish(String message) {
        if (!redisConfig.isUseRedis()) return;
        publish(redisConfig.getChannel(), message);
    }

    private void publish(String channel, String message) {
        getConnection().publish(channel, message);
    }

    @Override
    public void stop() {
        if (!redisConfig.isUseRedis()) return;
        getConnection().shutdown();
        threadPool.shutdown();
    }
}
