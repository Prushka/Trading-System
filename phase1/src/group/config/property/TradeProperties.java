package group.config.property;

import group.system.SaveHook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The Trade Property. The trade.properties file will be saved from resources to config by default.
 * File trade.properties is predefined in the resources root.
 *
 * @author Dan Lyu
 */
public class TradeProperties extends Property {

    /**
     * Constructs a TradeProperties object and save the file from resources root to the destination file.
     *
     * @param saveHook the properties will be saved by a saveHook
     */
    public TradeProperties(SaveHook saveHook) {
        try {
            saveDefault();
            properties.load(new FileInputStream(getFile()));
            saveHook.addSavable(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return config/trade.properties
     */
    @Override
    File getFile() {
        return new File("config/trade.properties");
    }
}
