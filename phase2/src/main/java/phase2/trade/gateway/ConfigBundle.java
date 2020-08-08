package phase2.trade.gateway;

import phase2.trade.Shutdownable;
import phase2.trade.config.yaml.ConfigStrategy;
import phase2.trade.config.yaml.PermissionConfig;
import phase2.trade.config.yaml.YamlStrategy;
import phase2.trade.user.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Supplier;

public class ConfigBundle implements Shutdownable {

    private PermissionConfig permissionConfig;

    // yaml strategy

    private ConfigStrategy configStrategy;

    public ConfigBundle() {
        configStrategy = new YamlStrategy();

        permissionConfig = read(PermissionConfig.class, permissionConfig.getFileName(), PermissionConfig::new);
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
        save(permissionConfig, "permission_group");
    }
}
