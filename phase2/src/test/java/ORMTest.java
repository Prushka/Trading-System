import org.hibernate.cfg.Configuration;
import org.junit.Test;
import phase2.trade.database.DatabaseResourceBundle;
import phase2.trade.database.UserDAO;
import phase2.trade.item.Item;
import phase2.trade.user.User;

import java.util.logging.Level;

public class ORMTest {

    private final DatabaseResourceBundle databaseResourceBundle;

    public ORMTest(){
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        databaseResourceBundle = new DatabaseResourceBundle();
    }

    @Test
    public void testItemManager() {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        Item item = new Item();
        item.setName("test item2");
        item.setDescription("test description2");

        UserDAO userDAO = new UserDAO(databaseResourceBundle);
        userDAO.openCurrentSessionWithTransaction();
        User user = userDAO.findById(1L);
        user.addItem(item);
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
    public void getItemFromUser() {


        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        UserDAO userDAO = new UserDAO(databaseResourceBundle);

        userDAO.openCurrentSession();

        User user = userDAO.findById(1L);
        System.out.println(user.getItems().get(0).getName());
        userDAO.closeCurrentSession();
    }
}
