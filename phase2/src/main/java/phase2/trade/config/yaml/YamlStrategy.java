package phase2.trade.config.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import phase2.trade.user.Permission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class YamlStrategy implements ConfigStrategy {

    @Override
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

    @Override
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
