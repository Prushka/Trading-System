package phase2.trade.command;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.Inventory;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

public class AddItemToItemList extends ItemCommand {

    private InventoryType inventoryType;

    private Long itemId;

    private PersonalUser operator;

    public AddItemToItemList(GatewayBundle gatewayBundle, PersonalUser operator,
                             InventoryType inventoryType, Long itemId) {
        super(gatewayBundle, Item.class, operator);
        this.inventoryType = inventoryType;
        this.itemId = itemId;
        this.operator = operator;
    }

    @Override
    public void execute(Callback<Item> callback, String... args) { //
        gatewayBundle.getUserGateway().submitSessionWithTransaction(() -> {
            Item item = findItemByIdSync(itemId);
            operator.getItemList(inventoryType).addItem(item);
            gatewayBundle.getUserGateway().update(operator);
            callback.call(item);
        });
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.ADD_ITEM);
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}