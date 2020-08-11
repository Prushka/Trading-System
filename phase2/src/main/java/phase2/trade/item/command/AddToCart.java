package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_WISH_LIST})
public class AddToCart extends ItemCommand<Item> {

    private Long itemId;

    public AddToCart(Long itemId) {
        this.itemId = itemId;
        addEffectedEntity(Item.class, itemId);
    }

    public AddToCart() {}

    @Override
    public void execute(StatusCallback<Item> callback, String... args) {
        if (!checkPermission()) {
            callback.call(null, ResultStatus.NO_PERMISSION);
            return;
        }
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            Item item = findItemByIdSyncOutsideItemGateway(itemId);
            operator.getItemList(ItemListType.CART).addItem(item);
            gateway.update(operator);
            callback.call(item, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public void undo() {
    }
}
