package group.config;

import group.config.property.LanguageProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public class FileHandlerFactory {

    private static FileHandler fileHandler;
    private final DateTimeFormatter dateTimeFormatter; // maybe as an instant variable

    public FileHandlerFactory() {
        dateTimeFormatter =
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd-HH-mm-ss")
                        .withZone(ZoneId.of("UTC-04:00"));
    }

    private void mkdirs() {
        try {
            Files.createDirectories(Paths.get("log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDate() {
        return dateTimeFormatter.format(Instant.now());
    }

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
