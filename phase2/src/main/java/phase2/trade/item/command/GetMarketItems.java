package phase2.trade.item.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;

import javax.persistence.Entity;
import java.util.List;

/**
 * The Command used to get all items that can be displayed in the Market.
 *
 * @author Dan Lyu
 */
@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false,
        persistent = false, permissionSet = {Permission.BrowseMarket})
public class GetMarketItems extends ItemCommand<List<Item>> {

    @Override
    public void execute(ResultStatusCallback<List<Item>> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getItemGateway().submitSession((gateway) -> callback.call(gateway.findMarketItems(), new StatusSucceeded()));
    }
}
