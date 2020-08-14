package phase2.trade.item.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.item.Willingness;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_PERSONAL_ITEMS})
public class AddItemToItemList extends ItemCommand<Item> {

    private ItemListType itemListType;

    private Long itemId;

    @Override
    public void execute(ResultStatusCallback<Item> callback, String... args) { // name, description, category, quantity, willingness, price (-1)
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            Item item = new Item();
            item.setName(args[0]);
            item.setDescription(args[1]);
            item.setCategory(Category.valueOf(args[2]));
            item.setQuantity(Integer.parseInt(args[3]));
            if (args.length > 4) {
                item.setWillingness(Willingness.valueOf(args[4]));
            }
            if (args.length > 5) {
                item.setPrice(Double.parseDouble(args[5]));
            }

            item.setOwner(operator);
            item.setOwnership(Ownership.TO_BE_REVIEWED);
            operator.getItemList(itemListType).addItem(item);
            gateway.update(operator);
            this.itemId = item.getUid();
            addEffectedEntity(Item.class, itemId);
            save();
            if (callback != null)
                callback.call(item, new StatusSucceeded());
        }, isAsynchronous());
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getItemGateway().submitTransaction((gateway) -> {
            gateway.delete(itemId);
            updateUndo();
        });
    }

    public void setItemListType(ItemListType itemListType) {
        this.itemListType = itemListType;
    }
}
