package phase2.trade.item.command;

import phase2.trade.command.CRUDType;
import phase2.trade.gateway.Callback;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
public class RemoveItem extends ItemCommand {

    private Long itemId;

    private transient RegularUser operator;

    private Ownership oldOwnership;

    private InventoryType inventoryType;

    public RemoveItem(EntityBundle entityBundle, RegularUser operator, InventoryType inventoryType, Long itemId) {
        super(entityBundle, operator);
        this.itemId = itemId;
        this.operator = operator;
        this.inventoryType = inventoryType;
    }

    public RemoveItem() {
        super();
    }

    @Override
    public void execute(Callback<Item> callback, String... args) { //
        entityBundle.getUserGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncInItemGateway(itemId);
            operator.getItemList(inventoryType).removeItem(item);

            addEffectedId(itemId);
            save();
            if (callback != null)
                callback.call(item);
        });
    }

    @Override
    public void undo() {
        entityBundle.getItemGateway().submitTransaction(() -> {
            updateUndo();
        });
    }

    @Override
    public void redo() {

    }


    @Override
    public CRUDType getCRUDType() {
        return CRUDType.UPDATE;
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.ADD_ITEM);
    }
}
