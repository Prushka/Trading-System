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
        persistent = true, permissionSet = {Permission.REVIEW_ITEM})
public class ReviewItem extends ItemCommand<Item> {

    private Long itemId;

    private Ownership oldOwnership;

    public ReviewItem(Long itemId) {
        this.itemId = itemId;
    }

    public ReviewItem() {}

    @Override
    public void execute(ResultStatusCallback<Item> callback, String... args) { //
        if (!checkPermission(callback)) return;
        getEntityBundle().getItemGateway().submitTransaction((gateway) -> {
            Item item = findItemByIdSyncInsideItemGateway(itemId);

            oldOwnership = item.getOwnership();
            item.setOwnership(Ownership.OWNER);
            gateway.update(item);
            addEffectedEntity(Item.class, itemId);
            save();
            if (callback != null)
                callback.call(item, new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getItemGateway().submitTransaction((gateway) -> {
            Item item = findItemByIdSyncInsideItemGateway(itemId);
            item.setOwnership(oldOwnership);
            updateUndo();
        });
    }

    @Override
    public void redo() {

    }
}
