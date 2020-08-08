import org.hibernate.cfg.Configuration;
import org.junit.Test;
import phase2.trade.callback.Callback;
import phase2.trade.gateway.*;
import phase2.trade.gateway.database.*;
import phase2.trade.item.Item;
import phase2.trade.item.ItemManager;
import phase2.trade.user.AccountManager;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import java.util.logging.Level;

public class ORMTest {

    private final EntityBundle bundle;

    private UserGateway userDAO;
    private ItemGateway itemDAO;

    public ORMTest() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        bundle = new DatabaseResourceBundle().getDaoBundle();

        userDAO = bundle.getUserGateway();
        itemDAO = bundle.getItemGateway();
    }

    @Test
    public void testAccount() {
        AccountManager accountManager = new AccountManager(bundle);
        accountManager.register(new Callback<User>() {
            @Override
            public void call(User result) {

            }
        }, "username", "email", "password", "country", "city");
    }

    @Test
    public void testItemManager() {

        Item item = new Item();
        item.setName("test item2");
        item.setDescription("test description2");

        userDAO.openCurrentSessionWithTransaction();
        User user = userDAO.findById(1L);
        userDAO.update(user);
        userDAO.closeCurrentSessionWithTransaction();


        // item.setOwner(user);

        // ItemDAO itemDAO = new ItemDAO(Item.class);
        // itemDAO.openCurrentSession();
        // itemDAO.add(item);
        // itemDAO.closeCurrentSession();
    }

    private User getTestUser() {
        userDAO.openCurrentSession();
        User user = userDAO.findById(1L);
        userDAO.closeCurrentSession();
        return user;
    }

    @Test
    public void getItemFromUser() throws InterruptedException {
        RegularUser user = new RegularUser("name", "email", "password", "country", "city");

        userDAO.submitSession(() -> userDAO.add(user), false);

    }
}
