package com.phase2.trade.config;


import com.phase2.trade.config.property.LanguageProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;

/**
 * A factory used to make sure only one instance of static FileHandler will be used to log data.<p>
 * This approach makes sure all information is logged in the same log file.
 *
 * @author Dan Lyu
 */
class FileHandlerFactory {

    /**
     * The single instance FileHandler to be used
     */
    private static FileHandler fileHandler;

    /**
     * The DateTimeFormatter used to format log file name
     */
    private final DateTimeFormatter dateTimeFormatter; // maybe as an instant variable

    /**
     * Constructs a FileHandlerFactory and initializes a DateTimeFormatter with pattern yyyy-MM-dd-HH-mm-ss in zone UTC-04:00.
     */
    FileHandlerFactory() {
        dateTimeFormatter =
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd-HH-mm-ss")
                        .withZone(ZoneId.of("UTC-04:00"));
    }

    /**
     * Makes parent directories of the log file if not exist.
     */
    private void mkdirs() {
        try {
            Files.createDirectories(Paths.get("log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The formatted date String in Zone UTC-04:00 and with pattern yyyy-MM-dd-HH-mm-ss
     */
    private String getDate() {
        return dateTimeFormatter.format(Instant.now());
    }

    /**
     * Returns an existing fileHandler.<p>
     * If the fileHandler doesn't exist, this method will create it.<p>
     *
     * @return the single instance file handler
     */
    public FileHandler getFileHandler() {
        if (fileHandler == null) {
            try {
                mkdirs();
                fileHandler = new FileHandler("log/" + getDate() + "-%u.log");
                fileHandler.setFormatter(new FileFormatter(new LanguageProperties()));
                fileHandler.setLevel(Level.ALL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileHandler;
    }
}
