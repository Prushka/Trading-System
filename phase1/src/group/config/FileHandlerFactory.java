package group.config;

import group.config.property.LanguageProperties;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;

public class FileHandlerFactory {

    private static FileHandler fileHandler;

    public FileHandler getFileHandler() {
        if (fileHandler == null) {
            try {
                fileHandler = new FileHandler("node.log");
                fileHandler.setFormatter(new FileFormatter(new LanguageProperties()));
                fileHandler.setLevel(Level.ALL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileHandler;
    }
}
