package phase2.trade.support.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.Command;
import phase2.trade.command.CommandProperty;
import phase2.trade.permission.Permission;
import phase2.trade.support.SupportTicket;
import phase2.trade.support.TicketPriority;
import phase2.trade.support.TicketState;
import phase2.trade.support.TicketType;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = false,
        persistent = true, permissionSet = {Permission.ManagePersonalSupportTickets})
public class OpenSupportTicket extends Command<SupportTicket> {

    @Override
    public void execute(ResultStatusCallback<SupportTicket> callback, String... args) { // content, priority, type
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            SupportTicket supportTicket = new SupportTicket();
            supportTicket.setSubmitter(operator);
            supportTicket.setContent(args[0]);
            supportTicket.setTicketPriority(TicketPriority.valueOf(args[1]));
            supportTicket.setType(TicketType.valueOf(args[2]));
            supportTicket.setTicketState(TicketState.PENDING_ALLOCATION);
            operator.getSupportTickets().add(supportTicket);
            gateway.update(operator);
            addEffectedEntity(SupportTicket.class, supportTicket.getUid());
            save();
            callback.call(supportTicket,new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getUserGateway().submitTransaction(gateway -> {
            gateway.delete(getOneEntity(User.class));
            updateUndo();
        });
    }
}
