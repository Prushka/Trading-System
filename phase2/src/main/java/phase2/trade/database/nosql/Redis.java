package phase2.trade.database.nosql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.Shutdownable;
import phase2.trade.config.RedisConfig;
import phase2.trade.controller.AbstractController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Redis implements Shutdownable {

    private static final Logger logger = LogManager.getLogger(Redis.class);

    private JedisPool jedisPool;

    private RedisConfig redisConfig;

    private final Map<String, AbstractController> registeredControllers;

    private final ExecutorService threadPool;

    public Redis(RedisConfig redisConfig, Map<String, AbstractController> registeredControllers) {

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

        this.redisConfig = redisConfig;
        subscribe();
    }

    public Jedis getConnection() {
        return jedisPool.getResource();
    }

    public void subscribe() {
        subscribe(redisConfig.getChannel());
    }

    public void subscribe(String channel) {
        JedisPubSub subscriber = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                logger.info(message);
                if (registeredControllers.containsKey(message)) {
                    registeredControllers.get(message).reload();
                }
            }
        };
        threadPool.submit(() -> {
            try {
                getConnection().subscribe(subscriber, channel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void publish(String message) {
        publish(redisConfig.getChannel(), message);
    }

    public void publish(String channel, String message) {
        getConnection().publish(channel, message);
    }

    @Override
    public void stop() {
        threadPool.shutdown();
    }
}
