import org.hibernate.cfg.Configuration;
import org.junit.Test;
import phase2.trade.database.*;
import phase2.trade.inventory.InventoryType;
import phase2.trade.inventory.ItemList;
import phase2.trade.item.Item;
import phase2.trade.item.ItemManager;
import phase2.trade.user.User;

import java.util.logging.Level;

public class ORMTest {

    private final DatabaseResourceBundle databaseResourceBundle;

    private UserDAO userDAO;
    private ItemDAO itemDAO;
    private DAO<ItemList> itemListDAO;

    public ORMTest() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        databaseResourceBundle = new DatabaseResourceBundleImpl();

        userDAO = new UserDAO(databaseResourceBundle);
        itemDAO = new ItemDAO(databaseResourceBundle);
        itemListDAO = new DAO<>(ItemList.class, databaseResourceBundle);
    }

    @Test
    public void testItemManager() {

        Item item = new Item();
        item.setName("test item2");
        item.setDescription("test description2");

        UserDAO userDAO = new UserDAO(databaseResourceBundle);
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
        UserDAO userDAO = new UserDAO(databaseResourceBundle);
        userDAO.openCurrentSession();
        User user = userDAO.findById(1L);
        userDAO.closeCurrentSession();
        return user;
    }

    @Test
    public void getItemFromUser() throws InterruptedException {
        // PersonalUser user = new PersonalUser("name", "email", "password");

        userDAO.openCurrentSession();
        User user = userDAO.findById(1);
        userDAO.closeCurrentSession();

        ItemManager itemManager = new ItemManager(databaseResourceBundle, user);

        itemManager.addItemTo(InventoryType.INVENTORY, new Callback<Item>() {
            @Override
            public void call(Item result) {
                System.out.println("123");
                System.out.println(result.getOwnership());
            }
        }, "TestCategory", "TestName", "TestDescription");

        Thread.sleep(10000);
        // itemDAO.openCurrentSessionWithTransaction();
        // System.out.println(user.getItemListMap());
        // itemDAO.closeCurrentSessionWithTransaction();
    }
}
