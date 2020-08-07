package phase2.trade.item;

import phase2.trade.database.Callback;
import phase2.trade.database.GatewayBundle;
import phase2.trade.inventory.InventoryType;
import phase2.trade.inventory.ItemList;
import phase2.trade.user.Permission;
import phase2.trade.user.PersonalUser;

public class ItemManager {

    private final GatewayBundle gatewayBundle;

    private final PersonalUser operator;

    public ItemManager(GatewayBundle gatewayBundle, PersonalUser operator) {
        this.gatewayBundle = gatewayBundle;
        this.operator = operator;
    }

    public void addItemTo(InventoryType inventoryType, Callback<Item> callback, Long itemId) {
        gatewayBundle.getUserGateway().submitSessionWithTransaction(() -> {
            gatewayBundle.getItemGateway().openCurrentSessionWithTransaction();
            Item item = gatewayBundle.getItemGateway().findById(itemId);
            gatewayBundle.getItemGateway().closeCurrentSessionWithTransaction();

            operator.getItemList(inventoryType).addItem(item);
            gatewayBundle.getUserGateway().update(operator);
            callback.call(item);
        });
    }

    public void createAndAddItemTo(InventoryType inventoryType, Callback<Item> callback, Category category, String name, String description) {
        gatewayBundle.getUserGateway().submitSessionWithTransaction(() -> {
            Item item = new Item();
            item.setCategory(category);
            item.setName(name);
            item.setDescription(description);
            item.setOwnership(Ownership.TO_BE_REVIEWED);

            operator.getItemList(inventoryType).addItem(item);
            gatewayBundle.getUserGateway().update(operator);
            callback.call(item);
        });
    }

    public void removeItemFrom(InventoryType inventoryType, Callback<Item> callback, Long itemId){
        gatewayBundle.getItemGateway().submitSessionWithTransaction(() -> {
            Item item = gatewayBundle.getItemGateway().findById(itemId);
            operator.getItemList(inventoryType).removeItem(item);
            callback.call(item);
        });
    }


    public void reviewItem(Callback<Boolean> callback, Ownership ownership, Long itemId) {
        if (operator.hasPermission(Permission.REVIEW_ITEM)) {
            gatewayBundle.getItemGateway().submitSessionWithTransaction(() -> {
                Item item = gatewayBundle.getItemGateway().findById(itemId);
                item.setOwnership(ownership);
                callback.call(true);
            });
        }
    }

    public void getInventory(InventoryType inventoryType, Callback<ItemList> callback) {
        gatewayBundle.getItemGateway().submitSessionWithTransaction(() -> {
            callback.call(operator.getItemList(inventoryType));
        });
    }

}
