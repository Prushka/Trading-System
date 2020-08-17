package phase2.trade.support.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.Command;
import phase2.trade.command.CommandProperty;
import phase2.trade.item.command.ItemCommand;
import phase2.trade.permission.Permission;
import phase2.trade.support.SupportTicket;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = false,
        persistent = false, permissionSet = {Permission.ManagePersonalSupportTickets})
public class GetSupportTickets extends Command<Collection<SupportTicket>> {

    @Override
    public void execute(ResultStatusCallback<Collection<SupportTicket>> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitSession((gateway) -> {

            Collection<SupportTicket> supports = operator.getSupportTickets(); // lazy initialize

            callback.call(supports, new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {

    }
}
