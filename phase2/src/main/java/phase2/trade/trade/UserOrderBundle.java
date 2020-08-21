package phase2.trade.trade;

import phase2.trade.itemlist.TradeItemHolder;
import phase2.trade.user.User;

import javax.persistence.*;

/**
 * The User order bundle.
 *
 * @author Dan Lyu
 * @author Grace Leung
 */
@Entity
public class UserOrderBundle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private TradeItemHolder tradeItemHolder; // the items this user is providing

    private int edits = 0;

    private boolean tradeConfirmed;

    private boolean transactionConfirmed;

    private boolean transactionBackConfirmed;

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets trade item holder.
     *
     * @return the trade item holder
     */
    public TradeItemHolder getTradeItemHolder() {
        return tradeItemHolder;
    }

    /**
     * Sets trade item holder.
     *
     * @param tradeItemHolder the trade item holder
     */
    public void setTradeItemHolder(TradeItemHolder tradeItemHolder) {
        this.tradeItemHolder = tradeItemHolder;
    }

    /**
     * Gets edits.
     *
     * @return the edits
     */
    public int getEdits() {
        return edits;
    }

    /**
     * Sets edits.
     *
     * @param edit the edit
     */
    public void setEdits(int edit) {
        this.edits = edit;
    }

    /**
     * Edit.
     */
    public void edit() {
        this.edits++;
    }

    /**
     * Is trade confirmed boolean.
     *
     * @return the boolean
     */
    public boolean isTradeConfirmed() {
        return tradeConfirmed;
    }

    /**
     * Sets trade confirmed.
     *
     * @param confirmation the confirmation
     */
    public void setTradeConfirmed(boolean confirmation) {
        this.tradeConfirmed = confirmation;
    }

    /**
     * Is transaction confirmed boolean.
     *
     * @return the boolean
     */
    public boolean isTransactionConfirmed() {
        return transactionConfirmed;
    }

    /**
     * Sets transaction confirmed.
     *
     * @param transactionConfirmed the transaction confirmed
     */
    public void setTransactionConfirmed(boolean transactionConfirmed) {
        this.transactionConfirmed = transactionConfirmed;
    }

    /**
     * Is transaction back confirmed boolean.
     *
     * @return the boolean
     */
    public boolean isTransactionBackConfirmed() {
        return transactionBackConfirmed;
    }

    /**
     * Sets transaction back confirmed.
     *
     * @param transactionBackConfirmed the transaction back confirmed
     */
    public void setTransactionBackConfirmed(boolean transactionBackConfirmed) {
        this.transactionBackConfirmed = transactionBackConfirmed;
    }
}
