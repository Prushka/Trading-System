package phase2.trade.item.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;
import java.util.List;

/**
 * The Command used to update the values of an Item.<p>
 * This Command can be used to update {@link Item}s the operator owns or {@link Item}s of other people.<p>
 * The permission depends on the Item owner.
 *
 * @author Dan Lyu
 */
@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true)
// The permission of this command depends on the operator and the item's owner
public class UpdateItems extends ItemCommand<Void> {

    private transient List<Item> itemsToUpdate;

    @Override
    public void execute(ResultStatusCallback<Void> callback, String... args) {
        if(!checkPermission(Permission.ManageAllItems)) {
            for (Item item : itemsToUpdate) {
                if (!item.getOwner().getUid().equals(operator.getUid())) {
                    checkPermission(callback, Permission.ManageAllItems); return;
                } else {
                    if (!checkPermission(callback, Permission.ManagePersonalItems)) return;
                }
            }
        }
        getEntityBundle().getItemGateway().submitTransaction((gateway) -> {
            for (Item item : itemsToUpdate) {
                gateway.update(item);
                addEffectedEntity(Item.class, item.getUid());
            }
            save();
            if (callback != null)
                callback.call(null, new StatusSucceeded());
        });
    }


    @Override
    protected void undoUnchecked() {
    }

    /**
     * Sets items to update.
     *
     * @param itemsToUpdate the items to update
     */
    public void setItemsToUpdate(List<Item> itemsToUpdate) {
        this.itemsToUpdate = itemsToUpdate;
    }
}
