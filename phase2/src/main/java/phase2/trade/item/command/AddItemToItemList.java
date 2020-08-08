package phase2.trade.item.command;

import phase2.trade.command.CRUDType;
import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.PersonalUser;

import javax.persistence.Entity;

@Entity
public class AddItemToItemList extends ItemCommand {

    private InventoryType inventoryType;

    private Long itemId;

    private transient PersonalUser operator;

    public AddItemToItemList(GatewayBundle gatewayBundle, PersonalUser operator,
                             InventoryType inventoryType) {
        super(gatewayBundle, operator);
        this.inventoryType = inventoryType;
        this.operator = operator;
    }

    public AddItemToItemList() {
        super();
    }

    @Override
    public void execute(Callback<Item> callback, String... args) { //
        gatewayBundle.getUserGateway().submitTransaction(() -> {
            Item item = new Item();
            item.setName(args[0]);
            item.setDescription(args[1]);

            operator.getItemList(inventoryType).addItem(item);
            gatewayBundle.getUserGateway().update(operator);
            this.itemId = item.getUid();
            addEffectedId(itemId);
            save();
            if (callback != null)
                callback.call(item);
        });
    }

    @Override
    public void undo() {
        gatewayBundle.getItemGateway().submitTransaction(() -> {
            gatewayBundle.getItemGateway().delete(itemId);
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