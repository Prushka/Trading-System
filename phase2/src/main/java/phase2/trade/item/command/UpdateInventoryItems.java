package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.permission.Permission;
import phase2.trade.user.RegularUser;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.List;
import java.util.Set;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_PERSONAL_ITEMS})
public class UpdateInventoryItems extends ItemCommand<Void> {

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> itemIds;

    private transient List<Item> itemsToUpdate;

    private Willingness newWillingness;

    // private Willingness oldWillingness;

    @Override
    public void execute(StatusCallback<Void> callback, String... args) {
        if (!checkPermission(callback)) return;
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
    public void undo() {
    }

    public void setNewWillingness(Willingness newWillingness) {
        this.newWillingness = newWillingness;
    }

    public void setItemsToUpdate(List<Item> itemsToUpdate) {
        this.itemsToUpdate = itemsToUpdate;
    }
}
