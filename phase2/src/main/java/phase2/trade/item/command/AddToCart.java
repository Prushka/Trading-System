package phase2.trade.item.command;

import phase2.trade.callback.status.StatusExist;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.editor.EntityIdLookUp;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {Permission.ManageWishList})
public class AddToCart extends ItemCommand<Void> {

    private final transient Set<Item> items = new HashSet<>();

    @Override
    public void execute(ResultStatusCallback<Void> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            for (Long uid : getEntityIds(items)) {
                if (operator.getItemList(ItemListType.CART).contains(uid)) {
                    callback.call(null, new StatusExist());
                    return;
                }
            }
            operator.getItemList(ItemListType.CART).addItem(items);
            gateway.merge(operator);
            callback.call(null, new StatusSucceeded());
        });
    }


    protected Set<Long> getEntityIds(Collection<Item> collection) {
        Set<Long> ids = new HashSet<>();
        collection.forEach(e -> ids.add(e.getUid()));
        return ids;
    }

    @Override
    protected void undoUnchecked() {
    }

    public void setItems(Item... items) {
        for (Item item : items) {
            addEffectedEntity(Item.class, item.getUid());
        }
        this.items.addAll(Arrays.asList(items));
    }
}
