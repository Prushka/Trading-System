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
        persistent = true, permissionSet = {Permission.ManagePersonalItems})
public class AddItemToItemList extends ItemCommand<Item> {


    private ItemListType itemListType;

    @Override
    public void execute(ResultStatusCallback<Item> callback, String... args) { // name, description, category, quantity, willingness, price (-1)
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {

            // logger.info(String.format("Adding Item: Name %s | Description %s | Category %s | Quantity %s | Willingness %s | Price %s",
            //         name, description, category, quantity, willingness, price));

            Item item = new Item();
            item.setName(argRequired(0, args));
            item.setDescription(argRequired(1, args));
            item.setCategory(Category.valueOf(argRequired(2, args)));
            item.setQuantity(Integer.parseInt(argRequired(3, "1", args)));
            item.setWillingness(Willingness.valueOf(argRequired(4, Willingness.Private.name(), args)));
            item.setPrice(Double.parseDouble(argRequired(5, "-1", args)));
            item.setOwner(operator);
            item.setOwnership(Ownership.TO_BE_REVIEWED);

            operator.getItemList(itemListType).addItem(item);
            gateway.update(operator);
            addEffectedEntity(Item.class, item.getUid());
            save();
            if (callback != null)
                callback.call(item, new StatusSucceeded());
        }, isAsynchronous());
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getItemGateway().submitTransaction((gateway) -> {
            gateway.delete(getOneEntity(Item.class));
            updateUndo();
        });
    }

    public void setItemListType(ItemListType itemListType) {
        this.itemListType = itemListType;
    }
}
