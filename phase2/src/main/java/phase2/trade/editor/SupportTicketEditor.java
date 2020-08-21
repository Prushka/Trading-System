package phase2.trade.editor;

import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.config.ConfigBundle;
import phase2.trade.support.SupportTicket;
import phase2.trade.support.TicketState;

import java.util.List;

/**
 * The {@link SupportTicket} editor.
 *
 * @author Dan Lyu
 * @see SupportTicket
 */
public class SupportTicketEditor extends Editor<SupportTicket> {

    /**
     * Constructs a new Support ticket editor.
     *
     * @param entities     the entities
     * @param configBundle the config bundle
     */
    public SupportTicketEditor(List<SupportTicket> entities, ConfigBundle configBundle) {
        super(entities, configBundle);
    }

    /**
     * Alter ticket state.
     *
     * @param state          the state
     * @param statusCallback the status callback
     */
    public void alterTicketState(String state, StatusCallback statusCallback) {
        entities.forEach(entity -> entity.setTicketState(TicketState.valueOf(state)));
        statusCallback.call(new StatusSucceeded());
    }
}
