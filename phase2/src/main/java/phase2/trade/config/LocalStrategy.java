package phase2.trade.config;

import phase2.trade.config.yaml.FormatStrategy;

import java.io.File;
import java.util.function.Supplier;

public class LocalStrategy implements ConfigStrategy {

    private FormatStrategy formatStrategy;

    public LocalStrategy(FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    public void changeStrategy(FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public <T extends ConfigDefaultable> T read(Class<T> configClass, String fileName, Supplier<T> supplier) { // These two methods could fit in ConfigBundle since all other strategies shall save and read local files
        File file = new File(fileName + formatStrategy.getExtension());
        if (!file.exists()) {
            T instantiated = supplier.get();
            instantiated.initDefault();
            return supplier.get();
        }
        return formatStrategy.read(configClass, file);
    }

    @Override
    public <T> void save(T entity, String fileName) {
        File file = new File(fileName + formatStrategy.getExtension());
        if (file.getParent() != null) {
            boolean success = new File(file.getParent()).mkdirs();
        }
        formatStrategy.save(entity, file);
    }
}
