package phase2.trade.support.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.item.command.ItemCommand;
import phase2.trade.permission.Permission;
import phase2.trade.support.SupportTicket;

import javax.persistence.Entity;
import java.util.List;

/**
 * The Get all support tickets.
 *
 * @author Dan Lyu
 */
@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false,
        persistent = false, permissionSet = {Permission.ManageAllSupportTickets})
public class GetAllSupportTickets extends ItemCommand<List<SupportTicket>> {

    @Override
    public void execute(ResultStatusCallback<List<SupportTicket>> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getSupportTicketGateway().submitSession((gateway) -> callback.call(gateway.findAll(), new StatusSucceeded()));
    }
}
