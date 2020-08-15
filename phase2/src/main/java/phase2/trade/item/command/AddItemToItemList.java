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
            String name = args[0];
            String description = args[1];
            String category = args[2];
            String quantity = args.length > 3 && args[3] != null ? args[3] : "1";
            String willingness = args.length > 4 && args[4] != null ? args[4] : Willingness.Private.name();
            String price = args.length > 5 && args[5] != null ? args[5] : "-1";

            // logger.info(String.format("Adding Item: Name %s | Description %s | Category %s | Quantity %s | Willingness %s | Price %s",
            //         name, description, category, quantity, willingness, price));

            Item item = new Item();
            item.setName(name);
            item.setDescription(description);
            item.setCategory(Category.valueOf(category));
            item.setQuantity(Integer.parseInt(quantity));
            item.setWillingness(Willingness.valueOf(willingness));
            item.setPrice(Double.parseDouble(price));
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
