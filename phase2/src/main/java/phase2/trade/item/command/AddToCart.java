package phase2.trade.item.command;

import phase2.trade.callback.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_WISH_LIST})
public class AddToCart extends ItemCommand<Void> {

    @Override
    public void execute(ResultStatusCallback<Void> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            for (Long itemId : getEffectedEntities(Item.class)) {
                Item item = findItemByIdSyncOutsideItemGateway(itemId);
                operator.getItemList(ItemListType.CART).addItem(item);
                gateway.update(operator);
            }
            callback.call(null, new StatusSucceeded());
        });
    }

    @Override
    public void undo() {
    }

    public void setItemIds(Long... itemIds) {
        for (Long itemId : itemIds) {
            addEffectedEntity(Item.class, itemId);
        }
    }
}
