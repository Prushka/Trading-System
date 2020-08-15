package phase2.trade.trade;

import phase2.trade.inventory.TradeItemHolder;
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
    private TradeItemHolder tradeItemHolder;

    private int edit;

    private boolean confirmation;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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
        return edit;
    }

    public void setEdits(int edit) {
        this.edit = edit;
    }

    public boolean getConfirmations() {
        return confirmation;
    }

    public void setConfirmations(boolean confirmation) {
        this.confirmation = confirmation;
    }
}
