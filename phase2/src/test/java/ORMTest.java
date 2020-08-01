import org.junit.Test;
import phase2.trade.controller.AccountManager;
import phase2.trade.database.UserDAO;
import phase2.trade.user.User;

public class ORMTest {

    @Test
    public void testAccountManager(){
        AccountManager accountManager = new AccountManager(new UserDAO());
        accountManager.register("test","email","password");
    }
}
