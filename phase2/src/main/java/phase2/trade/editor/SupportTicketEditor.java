package phase2.trade.editor;

import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.config.ConfigBundle;
import phase2.trade.support.SupportTicket;
import phase2.trade.support.TicketState;

import java.util.List;

public class SupportTicketEditor extends Editor<SupportTicket> {

    public SupportTicketEditor(List<SupportTicket> entities, ConfigBundle configBundle) {
        super(entities, configBundle);
    }

    public void alterTicketState(String state, StatusCallback statusCallback) {
        entities.forEach(entity -> entity.setTicketState(TicketState.valueOf(state)));
        statusCallback.call(new StatusSucceeded());
    }
}
