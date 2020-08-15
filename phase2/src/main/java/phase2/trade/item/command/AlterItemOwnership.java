package phase2.trade.item.command;

import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {Permission.ManageAllItems})
public class AlterItemOwnership extends ItemCommand<Item> {

    @Override
    public void execute(ResultStatusCallback<Item> callback, String... args) { // ownership
        if (!checkPermission(callback)) return;
        getEntityBundle().getItemGateway().submitTransaction((gateway) -> {
            Item item = gateway.findById(getOneEntity(Item.class));
            item.setOwnership(Ownership.valueOf(args[0]));
            // Item item = operator.getItemList(ItemListType.INVENTORY).findByUid(itemId);
            // item.setName(args[0]);
            // item.setDescription(args[1]);
            gateway.update(item);
            save();
            if (callback != null)
                callback.call(item, new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getItemGateway().submitTransaction((gateway) -> {
            getEffectedEntities(Item.class).forEach(gateway::delete);
            updateUndo();
        });
    }

    public void setItemId(Long... itemId) {
        addEffectedEntity(Item.class, itemId);
    }

}
