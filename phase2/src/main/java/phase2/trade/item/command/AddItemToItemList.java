package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.callback.Callback;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;

@Entity
public class AddItemToItemList extends ItemCommand {

    private InventoryType inventoryType;

    private Long itemId;

    private transient RegularUser operator;

    public AddItemToItemList(EntityBundle entityBundle, RegularUser operator,
                             InventoryType inventoryType) {
        super(entityBundle, operator);
        this.inventoryType = inventoryType;
        this.operator = operator;
    }

    public AddItemToItemList() {
        super();
    }

    @Override
    public void execute(StatusCallback<Item> callback, String... args) { //
        entityBundle.getUserGateway().submitTransaction(() -> {
            Item item = new Item();
            item.setName(args[0]);
            item.setDescription(args[1]);

            operator.getItemList(inventoryType).addItem(item);
            entityBundle.getUserGateway().update(operator);
            this.itemId = item.getUid();
            addEffectedId(itemId);
            save();
            if (callback != null)
                callback.call(item, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public void undo() {
        entityBundle.getItemGateway().submitTransaction(() -> {
            entityBundle.getItemGateway().delete(itemId);
            updateUndo();
        });
    }

    @Override
    public void redo() {

    }


    @Override
    public CRUDType getCRUDType() {
        return CRUDType.CREATE;
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.ADD_ITEM);
    }
}
