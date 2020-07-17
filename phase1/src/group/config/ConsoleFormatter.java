package group.config;

import group.config.property.LanguageProperties;

import java.util.Map;
import java.util.logging.LogRecord;

/**
 * The formatter used to log information using LanguageProperties and ansiColor.
 * The stacktrace won't be logged, instead a record with only message will be logged in a WARNING or SEVERE situation.
 * The stacktrace is handled in {@link FileFormatter}
 *
 * @author Dan Lyu
 */
class ConsoleFormatter extends LanguageFormatter {

    /**
     * @param lang Language Properties that maps the identifier to the text
     */
    ConsoleFormatter(LanguageProperties lang) {
        super(lang);
    }

    /**
     * Applies ansi color to all color identifiers
     *
     * @param message the raw message to be format
     * @return the formatted message with color texts to their ansi color representations
     */
    private String applyColor(String message) {
        message = "{BLACK}" + message; // the color is default to red in intellij idea
        for (Map.Entry<String, String> entry : ansiColor.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        return message;
    }

    /**
     * Returns a String with Language, colors, parameters applied and have an extra message in a WARNING or SEVERE situation.
     *
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
