package group.config;

import group.config.property.LanguageProperties;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.LogRecord;

/**
 * The formatter used to log information using LanguageProperties into log Files.
 *
 * @author Dan Lyu
 */
public class FileFormatter extends LanguageFormatter {

    private final DateTimeFormatter dateTimeFormatter;

    /**
     * @param lang Language Properties that map the identifier to the text
     */
    public FileFormatter(LanguageProperties lang) {
        super(lang);

        dateTimeFormatter =
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss ")
                        .withZone(ZoneId.of("UTC-04:00"));
    }


    private String removeColor(String message) {
        for (Map.Entry<String, String> entry : ansiColor.entrySet()) {
            message = message.replace(entry.getKey(), "");
        }
        return message;
    }

    private String getDate() {
        return dateTimeFormatter.format(Instant.now());
    }

    /**
     * @param record the record to log
     * @return the formatted String after applying language, parameters and ansiColor
     */
    @Override
    public String format(LogRecord record) {
        StringBuilder prefix = new StringBuilder(getDate());
        switch (record.getLevel().toString()) {
            case "FINE":
            case "FINER":
            case "FINEST":
                prefix.append("[DEBUG] ");
        }
        return prefix.toString() + removeColor(applyLanguage(record)) + "\n";
    }
}
