package group.config.property;

import group.system.SaveHook;

import java.io.File;

/**
 * The class for all trade properties. The trade.properties file will be saved from resources to config by default.<p>
 * File trade.properties is predefined in the resources root.<p>
 *
 * @author Dan Lyu
 */
public class TradeProperties extends Property {

    /**
     * Constructs a TradeProperties object and save the file from resources root to the destination file.<p>
     * Then the saveHook will manage the save process of the {@link #properties}.
     *
     * @param saveHook the properties will be saved by a saveHook
     */
    public TradeProperties(SaveHook saveHook) {
        super(saveHook);
    }

    /**
     * @return config/trade.properties
     */
    @Override
    File getFile() {
        return new File("config/trade.properties");
    }
}
