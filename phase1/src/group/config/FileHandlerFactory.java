package group.config;

import group.config.property.LanguageProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public class FileHandlerFactory {

    private static FileHandler fileHandler;

    private void mkdirs() {
        try {
            Files.createDirectories(Paths.get("log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileHandler getFileHandler() {
        if (fileHandler == null) {
            try {
                mkdirs();
                fileHandler = new FileHandler("log/" + System.currentTimeMillis() / 1000 + ".log");
                fileHandler.setFormatter(new FileFormatter(new LanguageProperties()));
                fileHandler.setLevel(Level.ALL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileHandler;
    }
}
