package phase2.trade.config.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import phase2.trade.config.strategy.FormatStrategy;

import java.io.File;
import java.io.IOException;

public class YamlStrategy implements FormatStrategy {

    public <T> T read(Class<T> configClass, File file) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        try {
            return mapper.readValue(file, configClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> void save(T entity, File file) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.writeValue(file, entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getExtension() {
        return ".yaml";
    }
}
