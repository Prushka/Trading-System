package phase2.trade.config.property;


import java.io.*;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Property class that wraps a Properties object.<p>
 *
 * @author Dan Lyu
 */
@Deprecated
public abstract class Property {

    /**
     * The java native Properties
     */
    final Properties properties = new Properties();


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
     * @param key the key to lookup
     * @return the value in boolean
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    /**
     * @param key          the key to lookup
     * @param defaultValue the default value if the value is missing
     * @return the value in boolean
     */
    public boolean getBoolean(String key, int defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(key, String.valueOf(defaultValue)));
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
