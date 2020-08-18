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
        persistent = false, permissionSet = {Permission.BrowseMarket})
public class GetMarketItems extends ItemCommand<List<Item>> {

    // TODO: Optimize the query here
    //  The owner and all its fields will be fetched together with its item
    //  This is unnecessary since we only need owner's name and address
    @Override
    public void execute(ResultStatusCallback<List<Item>> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getItemGateway().submitSession((gateway) -> callback.call(gateway.findMarketItems(), new StatusSucceeded()));
    }

    @Override
    protected void undoUnchecked() {

    }
}
