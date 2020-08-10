package phase2.trade.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemList;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.command.ItemCommand;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.READ, isUndoable = false, persistent = false, permissionSet = {Permission.BROWSE_USER_OPERATIONS})
public class GetCommands extends Command<List<Command>> {

    public GetCommands(GatewayBundle gatewayBundle, User operator) {
        super(gatewayBundle, operator);
        this.operator = operator;
    }

    public GetCommands() {
        super();
    }

    public GetCommands(GatewayBundle gatewayBundle) {
        super(gatewayBundle);
    }

    @Override
    public void execute(StatusCallback<List<Command>> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        getEntityBundle().getCommandGateway().submitSession(() -> callback.call(getEntityBundle().getCommandGateway().findAll(), ResultStatus.SUCCEEDED));
    }


    @Override
    public void undo() {
    }

}
