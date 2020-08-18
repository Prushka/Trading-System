package phase2.trade.config.strategy;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonStrategy implements FormatStrategy {

    public <T> T read(Class<T> configClass, File file) {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.findAndRegisterModules();
        try {
            return mapper.readValue(file, configClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> void save(T entity, File file) {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
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
