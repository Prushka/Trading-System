package group.config;

import group.config.property.LanguageProperties;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * The abstract formatter used to log information using LanguageProperties.
 *
 * @author Dan Lyu
 */
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

    /**
     * @param record the LogRecord
     * @return the formatted String after applying language and parameters
     */
    String applyLanguage(LogRecord record) {
        return lang.getMessage(record.getMessage(), record.getParameters());
    }

}
