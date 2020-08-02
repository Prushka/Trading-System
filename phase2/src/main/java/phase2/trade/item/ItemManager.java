package phase2.trade.item;

import phase2.trade.database.Callback;
import phase2.trade.database.ItemDAO;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemManager {

    private final ItemDAO itemDAO;

    public ItemManager(ItemDAO itemDAO){
        this.itemDAO = itemDAO;
    }

    public void addItem(Callback<Item> callback, String category, String name, String description){
        itemDAO.submitSessionWithTransaction(() -> {
            Item item = new Item();
            item.setCategory(category);
            item.setName(name);
            item.setDescription(description);
            itemDAO.add(item);
            callback.call(item);
        });
    }
}
