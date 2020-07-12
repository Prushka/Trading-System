package group.config.property;

import group.system.Savable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

/**
 * The Property class that wraps a Properties object.
 * It saves the Property from resources root to a destination file on need.
 *
 * @author Dan Lyu
 */
public abstract class Property implements Savable {

    /**
     * The java native Properties
     */
    final Properties properties = new Properties();

    /**
     * Saves the file from resources root the a destination file.
     *
     * @throws IOException if the file is not successfully saved from resources root to the destination file
     */
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

    /**
     * Saves the in memory properties to the destination file.
     */
    public void save() {
        try {
            properties.store(new FileOutputStream(getFile()), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the InputStream from the file in the resources root
     */
    InputStream getResource() {
        return getClass().getClassLoader().getResourceAsStream(getFile().getName());
    }

    /**
     * @param key          the key to lookup
     * @param defaultValue the default value if the value is missing
     * @return the value in String
     */
    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * @param key the key to lookup
     * @return the value in String
     */
    public String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * @param key the key to lookup
     * @return the value in int
     */
    public int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    /**
     * @param key          the key to lookup
     * @param defaultValue the default value if the value is missing
     * @return the value in Int
     */
    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
    }

    /**
     * @param key   the key to save the value to
     * @param value the value
     */
    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * @return the file of this property
     */
    abstract File getFile();

}
