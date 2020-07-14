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
        String affix = "";
        switch (record.getLevel().toString()) {
            case "WARNING":
            case "SEVERE":
                affix = " The stacktrace can be found in the most recent .log file in log folder"; // replacing this with a log file name will have to use another static variable in FileHandlerFactory
                break;
        }
        return applyColor(applyLanguage(record)) + ansiColor.get("{RESET}") + affix + "\n";
    }
}
