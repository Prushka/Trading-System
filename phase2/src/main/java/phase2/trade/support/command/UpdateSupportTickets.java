package phase2.trade.support.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.permission.Permission;
import phase2.trade.support.SupportTicket;
import phase2.trade.user.command.UserCommand;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true)
// The permission of this command depends on the operator and the item's owner
public class UpdateSupportTickets extends UserCommand<Void> {

    private transient List<SupportTicket> toUpdate;

    @Override
    public void execute(ResultStatusCallback<Void> callback, String... args) {
        if (!checkPermission(Permission.ManageAllSupportTickets)) { // the user doesn't have ManageUsers perm, this means he/she might be editing his/her own account
            for (SupportTicket user : toUpdate) {
                if (!user.getUid().equals(operator.getUid())) { // the user is editing other people's account, directly fire a StatusNoPermission and return
                    checkPermission(callback, Permission.ManageAllSupportTickets);
                    return;
                } else {
                    if (!checkPermission(callback, Permission.ManagePersonalSupportTickets)) // the user cannot even edit his/her own account
                        return;
                }
            }
        }
        getEntityBundle().getSupportTicketGateway().submitTransaction((gateway) -> {
            for (SupportTicket supportTicket : toUpdate) {
                gateway.update(supportTicket);
                addEffectedEntity(SupportTicket.class, supportTicket.getUid());
            }
            save();
            if (callback != null)
                callback.call(null, new StatusSucceeded());
        });
    }


    @Override
    protected void undoUnchecked() {
    }

    public void setToUpdate(List<SupportTicket> toUpdate) {
        this.toUpdate = toUpdate;
    }

    public void setUserToUpdate(SupportTicket toUpdate) {
        this.toUpdate = new ArrayList<>();
        this.toUpdate.add(toUpdate);
    }
}
