package phase2.trade.config.property;


import phase2.trade.repository.SaveHook;

import java.io.File;

public class SystemProperties  extends Property {

    /**
     * Constructs a TradeProperties object and save the file from resources root to the destination file.<p>
     * Then the saveHook will manage the save process of the {@link #properties}.
     *
     * @param saveHook the properties will be saved by a saveHook
     */
    public SystemProperties(SaveHook saveHook) {
        super(saveHook);
    }

    /**
     * @return com.phase2.com.phase2.trade.trade.config/com.phase2.trade.trade.properties
     */
    @Override
    File getFile() {
        return new File("system.properties");
    }
}
