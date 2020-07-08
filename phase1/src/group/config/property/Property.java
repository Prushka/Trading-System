package group.config.property;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public abstract class Property {

    final Properties properties = new Properties();

    public Property() {}

    abstract File getFile();

    InputStream getResource(){
        return getClass().getClassLoader().getResourceAsStream(getFile().getName());
    }

    public void saveDefault() throws IOException {
        if (!getFile().exists()) {
            System.out.println("file: " + getFile() + " not exist");
            InputStream inputStream = getResource();
            assert inputStream != null;
            if (getFile().getParent() != null) {
                boolean success = new File(getFile().getParent()).mkdirs();
            }
            Files.copy(inputStream, getFile().toPath());
        }
    }

    public void save() {
        try {
            properties.store(new FileOutputStream(getFile()), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public void set(String key, String value) {
        properties.setProperty(key, value);
    }
}
