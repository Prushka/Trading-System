package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;

@Entity
public class AddToCart extends ItemCommand {

    private InventoryType inventoryType;

    private Long itemId;

    private transient RegularUser operator;

    public AddToCart(EntityBundle entityBundle, RegularUser operator,
                     InventoryType inventoryType, Long itemId) {
        super(entityBundle, operator);
        this.inventoryType = inventoryType;
        this.itemId = itemId;
        this.operator = operator;
        addEffectedId(itemId);
    }

    public AddToCart() {
        super();
    }

    @Override
    public void execute(StatusCallback<Item> callback, String... args) { //
        if (!checkPermission()) {
            callback.call(null, ResultStatus.NO_PERMISSION);
            return;
        }
        entityBundle.getUserGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncOutsideItemGateway(itemId);
            operator.getItemList(inventoryType).addItem(item);
            entityBundle.getUserGateway().update(operator);
            callback.call(item, ResultStatus.SUCCEEDED);
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

    @Override
    public CRUDType getCRUDType() {
        return CRUDType.UPDATE;
    }
}
