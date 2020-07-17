package group.config;

import group.config.property.LanguageProperties;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class helps the instantiation of multiple loggers that have the same configuration.<p>
 * The class is compulsory to construct this class.<p>
 * A default logger can be created using {@link #getConfiguredLogger}.<p>
 * This default logger will have a FileLogger with ALL level and a ConsoleHandler with INFO level.
 *
 * @author Dan Lyu
 */
public class LoggerFactory {

    /**
     * The class to be used for the Logger
     */
    private final Class<?> clazz;

    /**
     * The level of the ConsoleHandler.<p>
     * If this is set to null, no ConsoleHandler will be added.<p>
     * By using default {@link #getConfiguredLogger} this field will have Level.INFO.
     */
    private Level consoleHandlerLevel;

    /**
     * The logger level to be used for the Logger object.<p>
     * By using default {@link #getConfiguredLogger} this field will have Level.ALL.
     */
    private final Level loggerLevel;

    /**
     * <code>true</code> will add a configured FileHandler to the Logger.<p>
     * This FileHandler has Level.ALL and is a single instance enforced by {@link FileHandlerFactory}.
     */
    private boolean useFileLogger = true;

    /**
     * @param clazz The class this logger logs
     * @param level the logger's logging level
     */
    public LoggerFactory(Class<?> clazz, Level level) {
        this.clazz = clazz;
        this.loggerLevel = level;
    }

    /**
     * Overloads {@link #LoggerFactory(Class, Level)} with Level.ALL.
     *
     * @param clazz The class this logger logs
     */
    public LoggerFactory(Class<?> clazz) {
        this.clazz = clazz;
        this.loggerLevel = Level.ALL;
    }

    /**
     * @param level The level to be used for the ConsoleHandler
     * @return the LoggerFactory itself
     */
    public LoggerFactory consoleHandler(Level level) {
        consoleHandlerLevel = level;
        return this;
    }

    /**
     * <code>true</code> will add a configured FileHandler to the Logger.<p>
     *
     * @return the LoggerFactory itself
     */
    public LoggerFactory useFileHandler(boolean value) {
        useFileLogger = value;
        return this;
    }

    /**
     * @return a default Level.ALL logger that has a FileLogger with ALL level and a ConsoleHandler with INFO level
     */
    public Logger getConfiguredLogger() {
        useFileHandler(true);
        consoleHandler(Level.INFO);
        return getLogger();
    }

    /**
     * @return a Logger using this Factory's configuration
     */
    public Logger getLogger() {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setUseParentHandlers(false);
        logger.setLevel(loggerLevel);
        if (consoleHandlerLevel != null) {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(consoleHandlerLevel);
            handler.setFormatter(new ConsoleFormatter(new LanguageProperties()));
            logger.addHandler(handler);
        }
        if (useFileLogger) {
            logger.addHandler(new FileHandlerFactory().getFileHandler());
        }
        return logger;
    }
}
