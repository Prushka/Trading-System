package phase2.trade.gateway;

import phase2.trade.Shutdownable;
import phase2.trade.config.*;
import phase2.trade.config.yaml.YamlStrategy;
import phase2.trade.config.PermissionConfig;

public class ConfigBundle implements Shutdownable {

    private final PermissionConfig permissionConfig;

    private final TradeConfig tradeConfig;

    private final DatabaseConfig databaseConfig;

    private ConfigStrategy configStrategy;


    public ConfigBundle() {
        configStrategy = new LocalStrategy(new YamlStrategy());

        permissionConfig = configStrategy.read(PermissionConfig.class, "config/permission_group", PermissionConfig::new);
        tradeConfig = configStrategy.read(TradeConfig.class, "config/trade", TradeConfig::new);
        databaseConfig = configStrategy.read(DatabaseConfig.class, "config/database", DatabaseConfig::new);
    }

    public void changeStrategy(ConfigStrategy configStrategy) {
        this.configStrategy = configStrategy;
    }


    @Override
    public void stop() {
        configStrategy.save(permissionConfig, "config/permission_group");
        configStrategy.save(tradeConfig, "config/trade");
        configStrategy.save(databaseConfig, "config/database");
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
}
