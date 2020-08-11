package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_PERSONAL_ITEMS})
public class AddItemToItemList extends ItemCommand<Item> {

    private ItemListType itemListType;

    private Long itemId;

    public AddItemToItemList(GatewayBundle gatewayBundle, User operator) {
        super(gatewayBundle, operator);
    }

    public AddItemToItemList() {}

    public void setItemListType(ItemListType itemListType) {
        this.itemListType = itemListType;
    }

    @Override
    public void execute(StatusCallback<Item> callback, String... args) { // name, description, category, quantity, price
        getEntityBundle().getUserGateway().submitTransaction(() -> {
            Item item = new Item();
            item.setName(args[0]);
            item.setDescription(args[1]);
            item.setCategory(Category.valueOf(args[2]));
            item.setQuantity(Integer.parseInt(args[3]));
            item.setPrice(Double.parseDouble(args[3]));

            item.setItemList(operator.getItemList(itemListType));
            operator.getItemList(itemListType).addItem(item);
            getEntityBundle().getUserGateway().update(operator);
            this.itemId = item.getUid();
            addEffectedEntity(Item.class, itemId);
            save();
            if (callback != null)
                callback.call(item, ResultStatus.SUCCEEDED);
        }, isAsynchronous());
    }

    @Override
    public void undo() {
        getEntityBundle().getItemGateway().submitTransaction(() -> {
            getEntityBundle().getItemGateway().delete(itemId);
            updateUndo();
        });
    }
}
