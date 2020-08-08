package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;

@Entity
public class AddToCart extends ItemCommand<Item> {

    private Long itemId;

    private transient RegularUser operator;

    public AddToCart(GatewayBundle gatewayBundle, RegularUser operator, Long itemId) {
        super(gatewayBundle, operator);
        this.itemId = itemId;
        this.operator = operator;
        addEffectedId(itemId);
    }

    public AddToCart() {
        super();
    }

    @Override
    public void execute(StatusCallback<Item> callback, String... args) {
        if (!checkPermission()) {
            callback.call(null, ResultStatus.NO_PERMISSION);
            return;
        }
        getEntityBundle().getUserGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncOutsideItemGateway(itemId);
            operator.getItemList(InventoryType.CART).addItem(item);
            getEntityBundle().getUserGateway().update(operator);
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
