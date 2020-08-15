package phase2.trade.database.nosql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.config.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class Redis {

    private JedisPool jedisPool;
    private RedisConfig redisConfig;

    private static final Logger logger = LogManager.getLogger(Redis.class);

    public Redis(RedisConfig redisConfig) {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisConfig.getPoolSize());

        this.jedisPool = new JedisPool(
                jedisPoolConfig,
                redisConfig.getHost(),
                redisConfig.getPort(),
                redisConfig.getTimeOut(),
                redisConfig.getPassword());

        this.redisConfig = redisConfig;
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
            }
        };
        new Thread(() -> {
            try {
                getConnection().subscribe(subscriber, channel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void publish(String message) {
        publish(redisConfig.getChannel(), message);
    }

    public void publish(String channel, String message) {
        getConnection().publish(channel, message);
    }

}
