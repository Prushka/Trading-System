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

    private int edits;

    private boolean hasConfirmed;

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

    public boolean hasConfirmed() {
        return hasConfirmed;
    }

    public void setConfirm(boolean confirmation) {
        this.hasConfirmed = confirmation;
    }
}
