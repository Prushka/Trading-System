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

    private final RedisConfig redisConfig;

    private Map<String, AbstractController> registeredControllers;

    private ExecutorService threadPool;


    public Redis(RedisConfig redisConfig, Map<String, AbstractController> registeredControllers) {

        this.redisConfig = redisConfig;
        if(!redisConfig.isUseRedis()) return;

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
        if(!redisConfig.isUseRedis()) return;
        subscribe(redisConfig.getChannel());
    }

    private void subscribe(String channel) {
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
        if(!redisConfig.isUseRedis()) return;
        publish(redisConfig.getChannel(), message);
    }

    private void publish(String channel, String message) {
        getConnection().publish(channel, message);
    }

    @Override
    public void stop() {
        threadPool.shutdown();
    }
}
