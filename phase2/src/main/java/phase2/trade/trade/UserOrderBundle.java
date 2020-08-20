package phase2.trade.trade;

import phase2.trade.itemlist.TradeItemHolder;
import phase2.trade.user.User;

import javax.persistence.*;

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

    public Long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TradeItemHolder getTradeItemHolder() {
        return tradeItemHolder;
    }

    public void setTradeItemHolder(TradeItemHolder tradeItemHolder) {
        this.tradeItemHolder = tradeItemHolder;
    }

    public int getEdits() {
        return edits;
    }

    public void setEdits(int edit) {
        this.edits = edit;
    }

    public void edit() {
        this.edits++;
    }

    public boolean isTradeConfirmed() {
        return tradeConfirmed;
    }

    public void setTradeConfirmed(boolean confirmation) {
        this.tradeConfirmed = confirmation;
    }

    public boolean isTransactionConfirmed() {
        return transactionConfirmed;
    }

    public void setTransactionConfirmed(boolean transactionConfirmed) {
        this.transactionConfirmed = transactionConfirmed;
    }

    public boolean isTransactionBackConfirmed() {
        return transactionBackConfirmed;
    }

    public void setTransactionBackConfirmed(boolean transactionBackConfirmed) {
        this.transactionBackConfirmed = transactionBackConfirmed;
    }
}
