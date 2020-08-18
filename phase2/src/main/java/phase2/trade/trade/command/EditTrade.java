package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeEditor;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true, persistent = true)
public class EditTrade extends TradeCommand<Trade> {
    private transient TradeEditor te;

    /*
    Once a transaction has been set up by one user on your system,
     the other user has to agree to the proposed trade.
     They also have to agree to a time and a place to meet.
      These two pieces of information should be entered by one of the users.
       The other can then confirm or edit.
       If they edit, then the original user can confirm or edit, and so on, until they both agree, up to a thresshold of 3 edits per user. If they both edit the time/place information three times without confirming, the system should print to the screen that their transaction has been cancelled.

Once the meeting has been set up, the associated transaction is open. To close it, both users have to confirm that the transaction took place, after the meeting was supposed to take place.

For a temporary transaction, the second meeting also has to be confirmed by both users, or else the transaction remains open, which counts towards both users potentially having their accounts frozen.
     */
    @Override
    public void execute(ResultStatusCallback<Trade> callback, String... args) {
        /*
        // TO DO: Implement cancelling after limit
        te = new TradeEditor(getConfigBundle().getTradeConfig().getEditLimit());
        if (!checkPermission(callback)) return;
        getEntityBundle().getTradeGateway().submitTransaction((gateway) -> {
            Trade currTrade = gateway.findById(tradeId);
            te.edit(currTrade, operator, args);
            gateway.update(currTrade);
            if (callback != null)
                callback.call(currTrade, new StatusSucceeded());
        });*/
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getTradeGateway().submitTransaction((gateway) -> {
            gateway.delete(tradeId);
            updateUndo();
        });
    }

}
