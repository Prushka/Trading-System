package group.config;

import group.config.property.LanguageProperties;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class helps the instantiation of multiple loggers that have the same configuration.<p>
 * The class is compulsory to construct this class.<p>
 * A default logger can be created using {@link #getConfiguredLogger}.<p>
 * This default logger will have a FileLogger with ALL level and a ConsoleLogger with INFO level.
 *
 * @author Dan Lyu
 */
public class LoggerFactory {

    private final Class<?> clazz;

    private Level consoleLoggerLevel;

    private final Level loggerLevel;

    private boolean useFileLogger = true;

    public LoggerFactory(Class<?> clazz, Level level) {
        this.clazz = clazz;
        this.loggerLevel = level;
    }

    public LoggerFactory(Class<?> clazz) {
        this.clazz = clazz;
        this.loggerLevel = Level.ALL;
    }

    public LoggerFactory consoleLoggerLevel(Level level) {
        consoleLoggerLevel = level;
        return this;
    }

    public LoggerFactory useFileLogger(boolean value) {
        useFileLogger = value;
        return this;
    }

    public Logger getConfiguredLogger() {
        useFileLogger(true);
        consoleLoggerLevel(Level.INFO);
        return getLogger();
    }

    public Logger getLogger() {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setUseParentHandlers(false);
        logger.setLevel(loggerLevel);
        if (consoleLoggerLevel != null) {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(consoleLoggerLevel);
            handler.setFormatter(new ConsoleColorFormatter(new LanguageProperties()));
            logger.addHandler(handler);
        }
        if (useFileLogger) {
            logger.addHandler(new FileHandlerFactory().getFileHandler());
        }
        return logger;
    }
}
