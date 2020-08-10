package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, isUndoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_WISH_LIST})
public class AddToCart extends ItemCommand<Item> {

    private Long itemId;

    private transient RegularUser operator;

    public AddToCart(GatewayBundle gatewayBundle, RegularUser operator, Long itemId) {
        super(gatewayBundle, operator);
        this.itemId = itemId;
        this.operator = operator;
        addEffectedEntity(Item.class, itemId);
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
            operator.getItemList(ItemListType.CART).addItem(item);
            getEntityBundle().getUserGateway().update(operator);
            callback.call(item, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public void undo() {
    }
}
