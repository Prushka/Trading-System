package phase2.trade.config.yaml;

import java.io.File;

public interface ConfigStrategy {
    <T> T read(Class<T> configClass, File file);

    <T> void save(T entity, File file);

    String getExtension();
}
