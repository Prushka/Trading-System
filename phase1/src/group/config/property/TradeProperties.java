package group.config.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TradeProperties extends Property {

    public TradeProperties(){
        try {
            saveDefault();
            properties.load(new FileInputStream(getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    File getFile() {
        return new File("config/trade.properties");
    }
}
