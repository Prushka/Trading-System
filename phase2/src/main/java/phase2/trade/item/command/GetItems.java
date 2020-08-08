package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.InventoryType;
import phase2.trade.inventory.ItemList;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class GetItems extends ItemCommand<ItemList> {

    private transient RegularUser operator;

    private InventoryType inventoryType;

    public GetItems(GatewayBundle gatewayBundle, RegularUser operator, InventoryType inventoryType) {
        super(gatewayBundle, operator);
        this.operator = operator;
        this.inventoryType = inventoryType;
    }

    public GetItems() {
        super();
    }

    @Override
    public void execute(StatusCallback<ItemList> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        ItemList itemList = operator.getItemList(inventoryType);
        callback.call(itemList, ResultStatus.SUCCEEDED);
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.ADD_ITEM);
    }


    @Override
    public void undo() {
    }

    @Override
    public void redo() {

    }

    @Override
    public CRUDType getCRUDType() {
        return CRUDType.UPDATE;
    }
}
