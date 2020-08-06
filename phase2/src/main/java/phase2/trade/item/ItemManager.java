package phase2.trade.item;

import phase2.trade.database.Callback;
import phase2.trade.database.DAOBundle;
import phase2.trade.inventory.InventoryType;
import phase2.trade.inventory.ItemList;
import phase2.trade.user.Permission;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

public class ItemManager {

    private final DAOBundle daoBundle;

    private final PersonalUser operator;

    public ItemManager(DAOBundle daoBundle, PersonalUser operator) {
        this.daoBundle = daoBundle;
        this.operator = operator;
    }

    public void addItemTo(InventoryType inventoryType, Callback<Item> callback, Long itemId) {
        daoBundle.getUserDAO().submitSessionWithTransaction(() -> {
            daoBundle.getItemDAO().openCurrentSessionWithTransaction();
            Item item = daoBundle.getItemDAO().findById(itemId);
            daoBundle.getItemDAO().closeCurrentSessionWithTransaction();

            operator.getItemList(inventoryType).addItem(item);
            daoBundle.getUserDAO().update(operator);
            callback.call(item);
        });
    }

    public void addItemTo(InventoryType inventoryType, Callback<Item> callback, Category category, String name, String description) {
        daoBundle.getUserDAO().submitSessionWithTransaction(() -> {
            Item item = new Item();
            item.setCategory(category);
            item.setName(name);
            item.setDescription(description);

            operator.getItemList(inventoryType).addItem(item);
            daoBundle.getUserDAO().update(operator);
            callback.call(item);
        });
    }
    /*public Item createNewItem(Callback<Item> callback, Category category, String name, String description) {
        Item item = new Item();
        item.setCategory(category);
        item.setName(name);
        item.setDescription(description);
        return item;
    }*/

    public void reviewItem(Callback<Boolean> callback, Ownership ownership, Long itemId) {
        if (operator.hasPermission(Permission.REVIEW_ITEM)) {
            daoBundle.getItemDAO().submitSessionWithTransaction(() -> {
                Item item = daoBundle.getItemDAO().findById(itemId);
                item.setOwnership(ownership);
                callback.call(true);
            });
        }
    }

    public void getInventory(InventoryType inventoryType, Callback<ItemList> callback) {
        daoBundle.getItemDAO().submitSessionWithTransaction(() -> {
            callback.call(operator.getItemList(inventoryType));
        });
    }

}
