package group.config;

import group.config.property.LanguageProperties;

import java.io.IOException;
import java.util.logging.FileHandler;

public class FileHandlerFactory {

    private static FileHandler fileHandler;

    public FileHandler getFileHandler() {
        if (fileHandler == null) {
            try {
                fileHandler = new FileHandler("node.log");
                fileHandler.setFormatter(new FileFormatter(new LanguageProperties()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileHandler;
    }
}
