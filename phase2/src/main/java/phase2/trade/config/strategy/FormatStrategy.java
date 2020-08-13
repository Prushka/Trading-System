package phase2.trade.config.strategy;

import java.io.File;
import java.util.function.Supplier;

public interface FormatStrategy {
    <T> T read(Class<T> configClass, File file);

    <T> void save(T entity, File file);

    String getExtension();
}
