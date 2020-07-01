package config.property;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

public abstract class Property {

    private final Properties properties = new Properties();

    public Properties getProperties() {
        return properties;
    }

    public Property() {
        try {
            saveDefault();
            properties.load(new FileInputStream(getFile()));
        }catch (IOException e){
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
}
