package group.config;

import group.config.property.LanguageProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleLanguageFormatter extends Formatter {

    private final Map<String, String> ansiColor = new HashMap<String, String>() {{
        put("{RESET}", "\u001B[0m");
        put("{BLACK}", "\u001B[30m");
        put("{RED}", "\u001B[31m");
        put("{GREEN}", "\u001B[32m");
        put("{YELLOW}", "\u001B[33m");
        put("{BLUE}", "\u001B[34m");
        put("{PURPLE}", "\u001B[35m");
        put("{CYAN}", "\u001B[36m");
        put("{WHITE}", "\u001B[37m");

        put("{BLACK_BACKGROUND}", "\u001B[40m");
        put("{RED_BACKGROUND}", "\u001B[41m");
        put("{GREEN_BACKGROUND}", "\u001B[42m");
        put("{YELLOW_BACKGROUND}", "\u001B[43m");
        put("{BLUE_BACKGROUND}", "\u001B[44m");
        put("{PURPLE_BACKGROUND}", "\u001B[45m");
        put("{CYAN_BACKGROUND}", "\u001B[46m");
        put("{WHITE_BACKGROUND}", "\u001B[47m");
    }};

    private final LanguageProperties lang;

    public ConsoleLanguageFormatter(LanguageProperties lang) {
        this.lang = lang;
    }

    public String formatColor(String message) {
        message = "{BLACK}" + message;
        for (Map.Entry<String, String> entry : ansiColor.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        return message;
    }

    @Override
    public String format(LogRecord record) {
        String prefix = "";
        switch (record.getLevel().toString()) {
            case "FINE":
            case "FINER":
            case "FINEST":
                prefix = ansiColor.get("{BLACK}") + "[DEBUG] ";
        }
        return prefix + formatColor(lang.getMessage(record.getMessage(), record.getParameters())) + ansiColor.get("{RESET}") + "\n";
    }
}
