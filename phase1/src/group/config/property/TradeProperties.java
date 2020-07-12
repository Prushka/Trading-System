package group.config.property;

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
     * Constructs a TradeProperties object and save the file from resources root to the destination file
     */
    public TradeProperties() {
        try {
            saveDefault();
            properties.load(new FileInputStream(getFile()));
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
