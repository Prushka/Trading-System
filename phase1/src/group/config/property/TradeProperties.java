package group.config.property;

import java.io.File;

public class TradeProperties extends Property {

    @Override
    public File getFile() {
        return new File("config/trade.properties");
    }
}
