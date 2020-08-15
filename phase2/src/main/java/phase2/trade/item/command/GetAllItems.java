package phase2.trade.item.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false,
        persistent = false, permissionSet = {Permission.BROWSE_MARKET})
public class GetAllItems extends ItemCommand<List<Item>> {

    @Override
    public void execute(ResultStatusCallback<List<Item>> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getItemGateway().submitSession((gateway) -> callback.call(gateway.findAll(), new StatusSucceeded()));
    }

    @Override
    protected void undoUnchecked() {

    }
}
