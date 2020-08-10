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
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, isUndoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_PERSONAL_ITEMS})
public class AlterItemInInventory extends ItemCommand<Item> {

    private Long itemId;

    public AlterItemInInventory(GatewayBundle gatewayBundle, User operator, Long itemId) {
        super(gatewayBundle, operator);
        this.itemId = itemId;
        this.operator = operator;
    }

    public AlterItemInInventory() {
        super();
    }

    @Override
    public void execute(StatusCallback<Item> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        getEntityBundle().getItemGateway().submitTransaction(() -> {
            Item item = operator.getItemList(ItemListType.INVENTORY).findByUid(itemId);
            item.setName(args[0]);
            item.setDescription(args[1]);
            getEntityBundle().getItemGateway().update(item);
            addEffectedEntity(Item.class, itemId);
            save();
            if (callback != null)
                callback.call(item, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public void undo() {
        getEntityBundle().getItemGateway().submitTransaction(() -> {
            getEntityBundle().getItemGateway().delete(itemId);
            updateUndo();
        });
    }

    @Override
    public void redo() {

    }
}
