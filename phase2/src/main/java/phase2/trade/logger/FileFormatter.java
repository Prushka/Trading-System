package phase2.trade.logger;

import phase2.trade.config.property.LanguageProperties;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.LogRecord;

/**
 * The formatter used to log information using LanguageProperties into log Files.<p>
 * The logged record has following format:<p>
 * yyyy-MM-dd HH:mm:ss [ClassSimpleName] [LEVEL] (left padding) - message<p>
 * The logged record will show stacktrace.
 *
 * @author Dan Lyu
 * @author print stacktrace from {@link LogRecord#getThrown()} - from {@link java.util.logging.SimpleFormatter}
 */
class FileFormatter extends LanguageFormatter {

    private final DateTimeFormatter dateTimeFormatter;

    /**
     * @param lang Language Properties that map the identifier to the text
     */
    FileFormatter(LanguageProperties lang) {
        super(lang);
        dateTimeFormatter =
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss ")
                        .withZone(ZoneId.of("UTC-04:00"));
    }


    /**
     * Removes color identifiers
     *
     * @param message the raw message to be format
     * @return the formatted message with all color texts removed
     */
    private String removeColor(String message) {
        for (Map.Entry<String, String> entry : ansiColor.entrySet()) {
            message = message.replace(entry.getKey(), "");
        }
        return message;
    }

    /**
     * @return The formatted date String in Zone UTC-04:00 and with pattern yyyy-MM-dd HH:mm:ss
     */
    private String getDate() {
        return dateTimeFormatter.format(Instant.now());
    }

    /**
     * @param record the record to log
     * @return the formatted String after applying language, parameters and ansiColor
     */
    @Override
    public String format(LogRecord record) {
        String className = record.getSourceClassName();
        String result = getDate() +
                " [" +
                className.substring((className.lastIndexOf(".")) + 1) +
                "]" + " [" + record.getLevel() + "] ";
        result = String.format("%-" + 45 + "s", result);
        result = result + "- " + removeColor(applyLanguage(record));
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            record.getThrown().printStackTrace(pw);
            pw.close();
            result += sw.toString();
        }
        return result + "\n";
    }
}
