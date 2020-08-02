import org.junit.Test;
import phase2.trade.controller.AccountManager;
import phase2.trade.database.Callback;
import phase2.trade.database.UserDAO;
import phase2.trade.item.ItemManager;
import phase2.trade.user.User;

import java.util.logging.Level;

public class ORMTest {

    @Test
    public void testAccountManager() {


        AccountManager accountManager = new AccountManager(new UserDAO());
    }

    @Test
    public void testItemManager() throws InterruptedException {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        AccountManager accountManager = new AccountManager(new UserDAO());
        accountManager.login(result -> {
            ItemManager itemManager = new ItemManager();
            itemManager.addItem(accountManager.getLoggedInUser());
        }, "aaa@bbb.ccc", "12345678");
        Thread.sleep(10000);
    }
}
