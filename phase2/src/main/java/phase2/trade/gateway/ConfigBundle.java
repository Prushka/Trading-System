package phase2.trade.gateway;

import phase2.trade.Shutdownable;
import phase2.trade.config.DatabaseConfig;
import phase2.trade.config.TradeConfig;
import phase2.trade.config.yaml.ConfigStrategy;
import phase2.trade.config.PermissionConfig;
import phase2.trade.config.yaml.YamlStrategy;

import java.io.File;
import java.util.function.Supplier;

public class ConfigBundle implements Shutdownable {

    private final PermissionConfig permissionConfig;

    private final TradeConfig tradeConfig;

    private ConfigStrategy configStrategy;

    private DatabaseConfig databaseConfig;

    public ConfigBundle() {
        configStrategy = new YamlStrategy();

        permissionConfig = read(PermissionConfig.class, "config/permission_group", PermissionConfig::new);
        tradeConfig = read(TradeConfig.class, "config/trade", TradeConfig::new);
        databaseConfig = read(DatabaseConfig.class, "config/database", DatabaseConfig::new);
    }

    public void changeStrategy(ConfigStrategy configStrategy) {
        this.configStrategy = configStrategy;
    }

    private <T> T read(Class<T> configClass, String fileName, Supplier<T> supplier) {
        File file = new File(fileName + configStrategy.getExtension());
        if (!file.exists()) return supplier.get();
        return configStrategy.read(configClass, file);
    }

    private <T> void save(T entity, String fileName) {
        File file = new File(fileName + configStrategy.getExtension());
        if (file.getParent() != null) {
            boolean success = new File(file.getParent()).mkdirs();
        }
        configStrategy.save(entity, file);
    }


    @Override
    public void stop() {
        save(permissionConfig, "config/permission_group");
        save(tradeConfig, "config/trade");
        save(databaseConfig, "config/database");
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
