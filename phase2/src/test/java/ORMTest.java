import org.junit.Test;
import phase2.trade.controller.AccountManager;
import phase2.trade.database.UserDAO;
import phase2.trade.user.User;

import java.util.logging.Level;

public class ORMTest {

    @Test
    public void testAccountManager(){

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        AccountManager accountManager = new AccountManager(new UserDAO());
    }
}
