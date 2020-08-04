package phase2.trade.item;

import phase2.trade.database.Callback;
import phase2.trade.database.ItemDAO;
import phase2.trade.inventory.Inventory;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemManager {

    private final ItemDAO itemDAO;

    private final User operator;

    public ItemManager(ItemDAO itemDAO, User operator) {
        this.itemDAO = itemDAO;
        this.operator = operator;
    }

    public void addItem(Callback<Item> callback, String category, String name, String description) {
        itemDAO.submitSessionWithTransaction(() -> {
            Item item = new Item();
            item.setCategory(category);
            item.setName(name);
            item.setDescription(description);
            item.setOwner(operator);
            item.setOwnership(Ownership.TO_BE_REVIEWED);
            itemDAO.add(item);
            callback.call(item);
        });
    }

    public void addItemToInventory() {

    }

    public void reviewItem(Callback<Boolean> callback, Ownership ownership, Long itemId) {
        if(operator.isAdmin()){
            itemDAO.submitSessionWithTransaction(() -> {
                Item item = itemDAO.findById(itemId);
                item.setOwnership(ownership);
            });
        }
    }

    public void getInventory(Callback<List<Item>> callback) {
        itemDAO.submitSessionWithTransaction(() -> {
            callback.call(operator.getInventory());
        });
    }

    public void getWishToBorrowList(Callback<List<Item>> callback) {
        itemDAO.submitSessionWithTransaction(() -> {
            callback.call(operator.getWishToBorrowList());
        });
    }

    public void getWishToLendList(Callback<List<Item>> callback) {
        itemDAO.submitSessionWithTransaction(() -> {
            callback.call(operator.getWishToLendList());
        });
    }

}
