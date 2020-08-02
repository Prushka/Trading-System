package phase2.trade.item;

import phase2.trade.database.ItemDAO;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemManager {

    private final ItemDAO itemDAO;


    private ExecutorService threadPool;

    public ItemManager(ItemDAO itemDAO){
        this.itemDAO = itemDAO;

        threadPool = Executors.newFixedThreadPool(3);
    }

    public void addItem(User owner){
        threadPool.submit(() -> {
            itemDAO.openCurrentSession();
            Item item = new Item();
            item.setName("item name");
            itemDAO.add(item);
            itemDAO.closeCurrentSession();
        });
    }
}
