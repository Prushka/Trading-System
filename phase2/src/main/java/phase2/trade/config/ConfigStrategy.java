package phase2.trade.config;

import java.util.function.Supplier;

public interface ConfigStrategy {

    <T extends ConfigDefaultable> T read(Class<T> configClass, String fileName, Supplier<T> supplier);

    <T> void save(T entity, String fileName);

}
