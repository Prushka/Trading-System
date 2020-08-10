package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.inventory.ItemList;
import phase2.trade.permission.Permission;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = true,
        persistent = true, permissionSet = {Permission.MANAGE_PERSONAL_ITEMS})
public class GetItems extends ItemCommand<ItemList> {

    private transient RegularUser operator;

    private ItemListType itemListType;

    public GetItems(GatewayBundle gatewayBundle, RegularUser operator, ItemListType itemListType) {
        super(gatewayBundle, operator);
        this.operator = operator;
        this.itemListType = itemListType;
    }

    public GetItems() {
        super();
    }

    @Override
    public void execute(StatusCallback<ItemList> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        ItemList itemList = operator.getItemList(itemListType);
        callback.call(itemList, ResultStatus.SUCCEEDED);
    }


    @Override
    public void undo() {
    }
}
