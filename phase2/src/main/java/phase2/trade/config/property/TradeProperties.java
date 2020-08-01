package phase2.trade.config.property;


import phase2.trade.repository.SaveHook;

import java.io.File;

/**
 * The class for all com.phase2.trade.trade properties. The com.phase2.trade.trade.properties file will be saved from resources to com.phase2.com.phase2.trade.trade.config by default.<p>
 * File com.phase2.trade.trade.properties is predefined in the resources root.<p>
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
     * @return com.phase2.com.phase2.trade.trade.config/com.phase2.trade.trade.properties
     */
    @Override
    File getFile() {
        return new File("trade.properties");
    }
}
