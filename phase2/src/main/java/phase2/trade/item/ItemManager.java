package phase2.trade.item;

import phase2.trade.database.Callback;
import phase2.trade.database.ItemDAO;
import phase2.trade.inventory.Inventory;
import phase2.trade.inventory.InventoryType;
import phase2.trade.user.User;

import java.util.List;

public class ItemManager {

    private final ItemDAO itemDAO;

    private final User operator;

    public ItemManager(ItemDAO itemDAO, User operator) {
        this.itemDAO = itemDAO;
        this.operator = operator;
    }

    public void addItemTo(InventoryType inventoryType, Callback<Item> callback, String category, String name, String description) {
        itemDAO.submitSessionWithTransaction(() -> {
            Item item = new Item();
            item.setCategory(category);
            item.setName(name);
            item.setDescription(description);
            operator.getItemListMap().get(inventoryType).addItem(item);
            itemDAO.add(item);
            callback.call(item);
        });
    }

    public void reviewItem(Callback<Boolean> callback, Ownership ownership, Long itemId) {
        if(operator.isAdmin()){
            itemDAO.submitSessionWithTransaction(() -> {
                Item item = itemDAO.findById(itemId);
                item.setOwnership(ownership);
                callback.call(true);
            });
        }
    }

    public void getInventory(InventoryType inventoryType, Callback<Boolean> callback) {
        itemDAO.submitSessionWithTransaction(() -> {

        });
    }

}
