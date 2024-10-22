package group.config.property;

import group.system.SaveHook;

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
     * @return config/trade.properties
     */
    @Override
    File getFile() {
        return new File("config/system.properties");
    }
}
