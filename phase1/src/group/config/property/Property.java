package group.config.property;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

public abstract class Property {

    private final Properties properties = new Properties();

    public Property() {
        try {
            saveDefault();
            properties.load(new FileInputStream(getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract File getFile();

    public void saveDefault() throws IOException {
        if (!getFile().exists()) {
            System.out.println("file: " + getFile() + " not exist");
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(getFile().getName());
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
