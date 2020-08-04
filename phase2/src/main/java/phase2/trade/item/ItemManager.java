package phase2.trade.item;

import phase2.trade.database.Callback;
import phase2.trade.database.DAOBundle;
import phase2.trade.inventory.InventoryType;
import phase2.trade.inventory.ItemList;
import phase2.trade.user.User;

public class ItemManager {

    private final DAOBundle daoBundle;

    private final User operator;

    public ItemManager(DAOBundle daoBundle, User operator) {
        this.daoBundle = daoBundle;
        this.operator = operator;
    }

    public void addItemTo(InventoryType inventoryType, Callback<Item> callback, String category, String name, String description) {
        daoBundle.getUserDAO().submitSessionWithTransaction(() -> {
            Item item = new Item();
            item.setCategory(category);
            item.setName(name);
            item.setDescription(description);

            operator.getItemList(inventoryType).addItem(item);
            System.out.println(operator.getItemList(inventoryType).getListOfItems().size());
            daoBundle.getUserDAO().update(operator);
            callback.call(item);
        });
    }

    public void reviewItem(Callback<Boolean> callback, Ownership ownership, Long itemId) {
        if (operator.isAdmin()) {
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
