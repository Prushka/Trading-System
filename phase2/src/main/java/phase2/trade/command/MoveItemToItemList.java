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

import javax.persistence.Entity;

@Entity
public class MoveItemToItemList extends ItemCommand {

    private InventoryType inventoryType;

    private Long itemId;

    private transient PersonalUser operator;

    public MoveItemToItemList(GatewayBundle gatewayBundle, PersonalUser operator,
                              InventoryType inventoryType, Long itemId) {
        super(gatewayBundle, operator);
        this.inventoryType = inventoryType;
        this.itemId = itemId;
        this.operator = operator;
        addEffectedId(itemId);
    }

    public MoveItemToItemList() {
        super();
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
    public void undo(GatewayBundle gatewayBundle) {

    }

    @Override
    public void redo(GatewayBundle gatewayBundle) {

    }
}
