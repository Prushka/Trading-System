package phase2.trade.item;

import phase2.trade.database.Callback;
import phase2.trade.database.DatabaseResourceBundle;
import phase2.trade.inventory.InventoryType;
import phase2.trade.inventory.ItemList;
import phase2.trade.user.User;

public class ItemManager {

    private final DatabaseResourceBundle databaseResourceBundle;

    private final User operator;

    public ItemManager(DatabaseResourceBundle databaseResourceBundle, User operator) {
        this.databaseResourceBundle = databaseResourceBundle;
        this.operator = operator;
    }

    public void addItemTo(InventoryType inventoryType, Callback<Item> callback, String category, String name, String description) {
        databaseResourceBundle.getUserDAO().submitSessionWithTransaction(() -> {
            Item item = new Item();
            item.setCategory(category);
            item.setName(name);
            item.setDescription(description);

            operator.getItemListMap().get(inventoryType).addItem(item);
            databaseResourceBundle.getUserDAO().update(operator);
            callback.call(item);
        });
    }

    public void reviewItem(Callback<Boolean> callback, Ownership ownership, Long itemId) {
        if (operator.isAdmin()) {
            databaseResourceBundle.getItemDAO().submitSessionWithTransaction(() -> {
                Item item = databaseResourceBundle.getItemDAO().findById(itemId);
                item.setOwnership(ownership);
                callback.call(true);
            });
        }
    }

    public void getInventory(InventoryType inventoryType, Callback<ItemList> callback) {
        databaseResourceBundle.getItemDAO().submitSessionWithTransaction(() -> {
            callback.call(operator.getItemListMap().get(inventoryType));
        });
    }

}
