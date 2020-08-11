package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.permission.Permission;
import phase2.trade.user.RegularUser;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.*;

@Entity
@CommandProperty(crudType = CRUDType.DELETE, undoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_PERSONAL_ITEMS})
public class RemoveItem extends ItemCommand<Long[]> {

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> itemIds;

    private Ownership oldOwnership;

    private ItemListType itemListType;

    public RemoveItem(ItemListType itemListType, Set<Long> itemIds) {
        this.itemIds = itemIds;
        this.itemListType = itemListType;
    }

    public RemoveItem() {}

    @Override
    public void execute(StatusCallback<Long[]> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        getEntityBundle().getUserGateway().submitTransaction(() -> {
            Long[] ids = itemIds.toArray(new Long[0]);
            operator.getItemList(itemListType).removeItemByUid(ids);
            getEntityBundle().getUserGateway().update(operator);
            addEffectedEntity(Item.class, ids);
            save();
            if (callback != null)
                callback.call(ids, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public void undo() {
        getEntityBundle().getItemGateway().submitTransaction(() -> {
            updateUndo();
        });
    }

    @Override
    public void redo() {

    }
}
