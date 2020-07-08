package group.config.property;

import java.io.File;

public class LanguageProperties extends Property {

    public String getMessage(String key, Object... paras) {
        String value = get(key, key);
        return String.format(value, paras);
    }

    @Override
    public File getFile() {
        return new File("config/language.properties");
    }
}
