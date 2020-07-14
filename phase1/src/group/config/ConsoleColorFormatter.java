package group.config;

import group.config.property.LanguageProperties;

import java.util.Map;
import java.util.logging.LogRecord;

/**
 * The formatter used to log information using LanguageProperties and ansiColor.
 *
 * @author Dan Lyu
 * @author shakram02 - <a href="https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println">print color in console</a>
 */
public class ConsoleColorFormatter extends LanguageFormatter {

    /**
     * @param lang Language Properties that maps the identifier to the text
     */
    public ConsoleColorFormatter(LanguageProperties lang) {
        super(lang);
    }

    /**
     * Applies ansi color
     *
     * @param message the raw message to format
     * @return the formatted message
     */
    private String applyColor(String message) {
        message = "{BLACK}" + message;
        for (Map.Entry<String, String> entry : ansiColor.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
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
                prefix = ansiColor.get("{BLACK}") + "[DEBUG] ";
        }
        return prefix + applyColor(applyLanguage(record)) + ansiColor.get("{RESET}") + "\n";
    }
}
