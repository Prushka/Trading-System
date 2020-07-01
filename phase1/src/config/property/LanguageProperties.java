package config.property;

import config.ConsoleLanguageFormatter;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LanguageProperties extends Property {

    public LanguageProperties() {
    }

    public String getMessage(String key, Object... paras) {
        String res = getProperties().getProperty(key);
        if (res == null) return key;
        return String.format(res, paras);
    }

    @Override
    public File getFile() {
        return new File("config/language.properties");
    }
}
