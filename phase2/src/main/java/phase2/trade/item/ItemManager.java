package phase2.trade.item;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.InventoryType;
import phase2.trade.inventory.ItemList;
import phase2.trade.user.Permission;
import phase2.trade.user.RegularUser;

public class ItemManager {

    private final GatewayBundle gatewayBundle;

    private final RegularUser operator;

    public ItemManager(GatewayBundle gatewayBundle, RegularUser operator) {
        this.gatewayBundle = gatewayBundle;
        this.operator = operator;
    }

    /*
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
*/
}
