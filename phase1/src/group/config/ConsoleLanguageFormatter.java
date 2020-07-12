package group.config;

import group.config.property.LanguageProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * The formatter used to log information using LanguageProperties and ansiColor.
 *
 * @author Dan Lyu
 * @author shakram02 - print color in console
 * @see <a href="https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println">print color in console</a>
 */
public class ConsoleLanguageFormatter extends Formatter {

    /**
     * The map of String identifier to AnsiColor
     */
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

        put("{BLACK_BOLD}", "\033[1;30m");
        put("{RED_BOLD}", "\033[1;31m");
        put("{GREEN_BOLD}", "\033[1;32m");
        put("{YELLOW_BOLD}", "\033[1;33m");
        put("{BLUE_BOLD}", "\033[1;34m");
        put("{PURPLE_BOLD}", "\033[1;35m");
        put("{CYAN_BOLD}", "\033[1;36m");
        put("{WHITE_BOLD}", "\033[1;37m");

        put("{BLACK_UNDERLINED}", "\033[4;30m");
        put("{RED_UNDERLINED}", "\033[4;31m");
        put("{GREEN_UNDERLINED}", "\033[4;32m");
        put("{YELLOW_UNDERLINED}", "\033[4;33m");
        put("{BLUE_UNDERLINED}", "\033[4;34m");
        put("{PURPLE_UNDERLINED}", "\033[4;35m");
        put("{CYAN_UNDERLINED}", "\033[4;36m");
        put("{WHITE_UNDERLINED}", "\033[4;37m");

        put("{BLACK_BRIGHT}", "\033[0;90m");
        put("{RED_BRIGHT}", "\033[0;91m");
        put("{GREEN_BRIGHT}", "\033[0;92m");
        put("{YELLOW_BRIGHT}", "\033[0;93m");
        put("{BLUE_BRIGHT}", "\033[0;94m");
        put("{PURPLE_BRIGHT}", "\033[0;95m");
        put("{CYAN_BRIGHT}", "\033[0;96m");
        put("{WHITE_BRIGHT}", "\033[0;97m");
    }};

    /**
     * Language Properties that predefines the identifier to the text
     */
    private final LanguageProperties lang;

    /**
     * @param lang Language Properties that predefines the identifier to the text
     */
    public ConsoleLanguageFormatter(LanguageProperties lang) {
        this.lang = lang;
    }

    /**
     * Applies ansi color
     *
     * @param message the raw message to format
     * @return the formatted message
     */
    public String formatColor(String message) {
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
        return prefix + formatColor(lang.getMessage(record.getMessage(), record.getParameters())) + ansiColor.get("{RESET}") + "\n";
    }
}
