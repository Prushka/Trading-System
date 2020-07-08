package group.config.property;

import java.io.File;
import java.io.IOException;

public class LanguageProperties extends Property {

    public String getMessage(String key, Object... paras) {
        String value = get(key, key);
        return String.format(value, paras);
    }

    @Override
    File getFile() {
        return new File("config/language.properties");
    }

    public LanguageProperties(){
        try {
            properties.load(getResource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
