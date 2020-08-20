package phase2.trade.item.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.itemlist.ItemListType;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.Set;

@Entity
@CommandProperty(crudType = CRUDType.DELETE, undoable = true,
        persistent = true)
public class RemoveItem extends ItemCommand<Long[]> {

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> itemIds;

    private Ownership oldOwnership;

    private ItemListType itemListType;

    @Override
    public void execute(ResultStatusCallback<Long[]> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            Long[] ids = itemIds.toArray(new Long[0]);
            operator.getItemList(itemListType).removeItemByUid(ids);
            gateway.update(operator);
            addEffectedEntity(Item.class, ids);
            save();
            if (callback != null)
                callback.call(ids, new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getItemGateway().submitTransaction((gateway) -> {
            updateUndo();
        });
    }

    public void setItemListType(ItemListType itemListType) {
        this.itemListType = itemListType;
    }

    public void setItemIds(Set<Long> itemIds) {
        this.itemIds = itemIds;
    }
}
