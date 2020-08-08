package phase2.trade.item;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
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

    private Item findItemByIdSync(Long itemId) {
        gatewayBundle.getItemGateway().openCurrentSessionWithTransaction();
        Item item = gatewayBundle.getItemGateway().findById(itemId);
        gatewayBundle.getItemGateway().closeCurrentSessionWithTransaction();
        return item;
    }

    public void addItemTo(InventoryType inventoryType, Callback<Item> callback, Long itemId) {
        gatewayBundle.getUserGateway().submitTransaction(() -> {
            Item item = findItemByIdSync(itemId);

            operator.getItemList(inventoryType).addItem(item);
            gatewayBundle.getUserGateway().update(operator);
            callback.call(item);
        });
    }

    public void createAndAddItemToInventory(InventoryType inventoryType, Callback<Item> callback, Category category, String name, String description) {
        gatewayBundle.getUserGateway().submitTransaction(() -> {
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

    public void createAndAddItemTo(InventoryType inventoryType, Callback<Item> callback, Category category, String name, String description) {
        gatewayBundle.getUserGateway().submitTransaction(() -> {
            Item item = new Item();
            item.setCategory(category);
            item.setName(name);
            item.setDescription(description);

            operator.getItemList(inventoryType).addItem(item);
            gatewayBundle.getUserGateway().update(operator);
            callback.call(item);
        });
    }

    public void removeItemFrom(InventoryType inventoryType, Callback<Item> callback, Long itemId) {
        gatewayBundle.getItemGateway().submitTransaction(() -> {
            Item item = gatewayBundle.getItemGateway().findById(itemId);
            operator.getItemList(inventoryType).removeItem(item);
            callback.call(item);
        });
    }


    public void reviewItem(Callback<Boolean> callback, Ownership ownership, Long itemId) {
        if (operator.hasPermission(Permission.REVIEW_ITEM)) {
            gatewayBundle.getItemGateway().submitTransaction(() -> {
                Item item = gatewayBundle.getItemGateway().findById(itemId);
                item.setOwnership(ownership);
                callback.call(true);
            });
        }
    }

    public void getInventory(InventoryType inventoryType, Callback<ItemList> callback) {
        gatewayBundle.getItemGateway().submitTransaction(() -> {
            callback.call(operator.getItemList(inventoryType));
        });
    }

}
