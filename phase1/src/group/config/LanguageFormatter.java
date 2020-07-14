package group.config;

import group.config.property.LanguageProperties;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public abstract class LanguageFormatter extends Formatter {

    /**
     * Language Properties that predefines the identifier to the text
     */
    private final LanguageProperties lang;

    /**
     * @param lang Language Properties that map the identifier to the text
     */
    public LanguageFormatter(LanguageProperties lang) {
        this.lang = lang;
    }

    String applyLanguage(LogRecord record) {
        return lang.getMessage(record.getMessage(), record.getParameters());
    }

}
