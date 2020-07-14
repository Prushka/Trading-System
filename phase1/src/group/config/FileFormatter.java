package group.config;

import group.config.property.LanguageProperties;

import java.util.Map;
import java.util.logging.LogRecord;

/**
 * The formatter used to log information using LanguageProperties into log Files.
 *
 * @author Dan Lyu
 */
public class FileFormatter extends LanguageFormatter {

    /**
     * @param lang Language Properties that map the identifier to the text
     */
    public FileFormatter(LanguageProperties lang) {
        super(lang);
    }


    private String removeColor(String message) {
        for (Map.Entry<String, String> entry : ansiColor.entrySet()) {
            message = message.replace(entry.getKey(), "");
        }
        return message;
    }

    /**
     * @param record the record to log
     * @return the formatted String after applying language, parameters and ansiColor
     */
    @Override
    public String format(LogRecord record) {
        String prefix = "";
        switch (record.getLevel().toString()) {
            case "FINE":
            case "FINER":
            case "FINEST":
                prefix = "[DEBUG] ";
        }
        return prefix + removeColor(applyLanguage(record)) + "\n";
    }
}
