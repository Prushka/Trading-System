package com.phase2.trade.config.property;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * The Language Property. The language.properties file will not be saved from resources to com.phase2.com.phase2.trade.trade.config by default.
 * File language.properties is predefined in the resources root.
 *
 * @author Dan Lyu
 */
public class LanguageProperties extends Property {

    /**
     * Constructs a LanguageProperties
     */
    public LanguageProperties() {
        try {
            properties.load(getResource());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Cannot initialize the property!", e);
        }
    }

    /**
     * @param key   the identifier
     * @param paras the parameters to be formatted into identifier
     * @return the language text
     */
    public String getMessage(String key, Object... paras) {
        String value = get(key, key);
        return String.format(value, paras);
    }

    /**
     * @return language.properties
     */
    @Override
    File getFile() {
        return new File("language.properties");
    }

}
