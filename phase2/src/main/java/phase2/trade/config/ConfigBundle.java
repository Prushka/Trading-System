package phase2.trade.config;

import phase2.trade.Shutdownable;
import phase2.trade.config.*;
import phase2.trade.config.strategy.ConfigStrategy;
import phase2.trade.config.strategy.LocalStrategy;
import phase2.trade.config.strategy.YamlStrategy;
import phase2.trade.config.PermissionConfig;

public class ConfigBundle implements Shutdownable {

    private final PermissionConfig permissionConfig;

    private final TradeConfig tradeConfig;

    private final DatabaseConfig databaseConfig;

    private final UIConfig uiConfig;

    private final RedisConfig redisConfig;

    private final GeoConfig geoConfig;

    private ConfigStrategy configStrategy;


    public ConfigBundle() {
        configStrategy = new LocalStrategy(new YamlStrategy());

        permissionConfig = configStrategy.read(PermissionConfig.class, "config/permission_group", PermissionConfig::new);
        tradeConfig = configStrategy.read(TradeConfig.class, "config/trade", TradeConfig::new);
        databaseConfig = configStrategy.read(DatabaseConfig.class, "config/database", DatabaseConfig::new);
        uiConfig = configStrategy.read(UIConfig.class, "config/ui", UIConfig::new);
        redisConfig = configStrategy.read(RedisConfig.class, "config/redis", RedisConfig::new);
        geoConfig = new GeoConfig();
    }

    public void changeStrategy(ConfigStrategy configStrategy) {
        this.configStrategy = configStrategy;
    }


    @Override
    public void stop() {
        configStrategy.save(permissionConfig, "config/permission_group");
        configStrategy.save(tradeConfig, "config/trade");
        configStrategy.save(databaseConfig, "config/database");
        configStrategy.save(uiConfig, "config/ui");
        configStrategy.save(redisConfig, "config/redis");
    }

    public PermissionConfig getPermissionConfig() {
        return permissionConfig;
    }

    public TradeConfig getTradeConfig() {
        return tradeConfig;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public UIConfig getUiConfig() {
        return uiConfig;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public GeoConfig getGeoConfig() {
        return geoConfig;
    }
}
