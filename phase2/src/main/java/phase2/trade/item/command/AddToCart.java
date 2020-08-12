package phase2.trade.item.command;

import phase2.trade.callback.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_WISH_LIST})
public class AddToCart extends ItemCommand<Void> {

    private transient List<Item> items;

    @Override
    public void execute(ResultStatusCallback<Void> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            System.out.println(items.get(0).getUid());
            System.out.println(gateway.getEntityManager());
            operator.getItemList(ItemListType.CART).addItem(gateway.getEntityManager().getReference(Item.class, items.get(0).getUid()));
            gateway.merge(operator);
            callback.call(null, new StatusSucceeded());
        });
    }

    @Override
    public void undo() {
    }

    public void setItems(Item... items) {
        for (Item item : items) {
            addEffectedEntity(Item.class, item.getUid());
        }
        this.items = Arrays.asList(items);
    }
}
