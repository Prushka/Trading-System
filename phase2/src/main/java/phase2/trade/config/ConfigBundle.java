package phase2.trade.config;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import phase2.trade.Main;
import phase2.trade.Shutdownable;
import phase2.trade.config.strategy.ConfigStrategy;
import phase2.trade.config.strategy.LocalStrategy;
import phase2.trade.config.strategy.YamlStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * The Config bundle that keeps all config entities.
 *
 * @author Dan Lyu
 */
public class ConfigBundle implements Shutdownable {

    private final PermissionConfig permissionConfig;

    private final TradeConfig tradeConfig;

    private final DatabaseConfig databaseConfig;

    private final UIConfig uiConfig;

    private final RedisConfig redisConfig;

    private final GeoConfig geoConfig;

    private ConfigStrategy configStrategy;


    /**
     * Constructs a new Config bundle.
     */
    public ConfigBundle() {
        configStrategy = new LocalStrategy(new YamlStrategy());

        permissionConfig = configStrategy.read(PermissionConfig.class, "config/permission_group", PermissionConfig::new);
        tradeConfig = configStrategy.read(TradeConfig.class, "config/trade", TradeConfig::new);
        databaseConfig = configStrategy.read(DatabaseConfig.class, "config/database", DatabaseConfig::new);
        uiConfig = configStrategy.read(UIConfig.class, "config/ui", UIConfig::new);
        redisConfig = configStrategy.read(RedisConfig.class, "config/redis", RedisConfig::new);
        geoConfig = new GeoConfig(getGeoJson());
        save();
    }

    private JSONObject getGeoJson() {
        InputStream is = Main.class.getResourceAsStream("/json/ca.json");
        String jsonTxt;
        try {
            jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            return new JSONObject(jsonTxt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    /**
     * Change config strategy.
     *
     * @param configStrategy the config strategy
     */
    public void changeStrategy(ConfigStrategy configStrategy) {
        this.configStrategy = configStrategy;
    }


    @Override
    public void stop() {
        save();
    }

    private void save() {
        configStrategy.save(permissionConfig, "config/permission_group");
        configStrategy.save(tradeConfig, "config/trade");
        configStrategy.save(databaseConfig, "config/database");
        configStrategy.save(uiConfig, "config/ui");
        configStrategy.save(redisConfig, "config/redis");
    }

    /**
     * Gets permission config.
     *
     * @return the permission config
     */
    public PermissionConfig getPermissionConfig() {
        return permissionConfig;
    }

    /**
     * Gets trade config.
     *
     * @return the trade config
     */
    public TradeConfig getTradeConfig() {
        return tradeConfig;
    }

    /**
     * Gets database config.
     *
     * @return the database config
     */
    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    /**
     * Gets ui config.
     *
     * @return the ui config
     */
    public UIConfig getUiConfig() {
        return uiConfig;
    }

    /**
     * Gets redis config.
     *
     * @return the redis config
     */
    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    /**
     * Gets geo config.
     *
     * @return the geo config
     */
    public GeoConfig getGeoConfig() {
        return geoConfig;
    }
}
