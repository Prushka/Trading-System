package phase2.trade.config.property;


import phase2.trade.logger.LoggerFactory;
import phase2.trade.repository.Savable;
import phase2.trade.repository.SaveHook;

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
public abstract class Property implements Savable {

    static final Logger LOGGER = new LoggerFactory(Property.class).getConfiguredLogger();

    /**
     * The java native Properties
     */
    final Properties properties = new Properties();

    /**
     * Constructs a Property object and save the file from resources root to the destination file.<p>
     * Then the saveHook will manage the save process of the {@link #properties}.
     *
     * @param saveHook the properties will be saved by a saveHook
     */
    public Property(SaveHook saveHook) {
        try {
            saveDefault();
            properties.load(new FileInputStream(getFile()));
            saveHook.addSavable(this);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Cannot initialize the property!", e);
        }
    }

    /**
     * Empty constructor to be used by subclasses that don't save by default and don't save by {@link group.system.SaveHook}.
     */
    public Property() {
    }

    /**
     * Saves the file from resources root the a destination file.
     *
     * @throws IOException if the file is not successfully saved from resources root to the destination file
     */
    public void saveDefault() throws IOException {
        if (!getFile().exists()) {
            // System.out.println("file: " + getFile() + " not exist");
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
            LOGGER.log(Level.SEVERE, "Cannot save the property to file!", e);
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
