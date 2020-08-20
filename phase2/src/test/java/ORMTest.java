import org.junit.Test;
import phase2.trade.config.ConfigBundle;
import phase2.trade.database.DatabaseResourceBundle;
import phase2.trade.gateway.EntityGatewayBundle;
import phase2.trade.gateway.ItemGateway;
import phase2.trade.gateway.UserGateway;

import java.util.logging.Level;

public class ORMTest {

    private final EntityGatewayBundle bundle;

    private UserGateway userDAO;
    private ItemGateway itemDAO;

    public ORMTest() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        ConfigBundle configBundle = new ConfigBundle();
        bundle = new DatabaseResourceBundle(configBundle.getDatabaseConfig()).getEntityGatewayBundle();

        userDAO = bundle.getUserGateway();
        itemDAO = bundle.getItemGateway();
    }

    @Test
    public void testItemCommands() {

    }


}
