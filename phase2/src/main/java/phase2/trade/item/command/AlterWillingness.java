package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
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
import java.util.Set;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_PERSONAL_ITEMS})
public class AlterWillingness extends ItemCommand<Item> {

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> itemIds;

    private Willingness newWillingness;

    // private Willingness oldWillingness;

    @Override
    public void execute(StatusCallback<Item> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            Long[] ids = itemIds.toArray(new Long[0]);
            for (Item item : operator.getItemList(ItemListType.INVENTORY).getListOfItems()) {
                if (itemIds.contains(item.getUid())) {
                    item.setWillingness(newWillingness);
                }
            }
            gateway.update(operator);
            addEffectedEntity(Item.class, ids);
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

    public void setItemIds(Set<Long> itemIds) {
        this.itemIds = itemIds;
    }
}
