package phase2.trade.item.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.inventory.ItemListType;
import phase2.trade.inventory.ItemList;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = true,
        persistent = true, permissionSet = {Permission.ManagePersonalItems})
public class GetItems extends ItemCommand<ItemList> {

    private ItemListType itemListType;

    @Override
    public void execute(ResultStatusCallback<ItemList> callback, String... args) {
        if (!checkPermission(callback)) return;
        ItemList itemList = operator.getItemList(itemListType);
        callback.call(itemList, new StatusSucceeded());
    }


    @Override
    protected void undoUnchecked() {
    }
}
