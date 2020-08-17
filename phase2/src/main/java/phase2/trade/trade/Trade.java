package phase2.trade.trade;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<TradeOrder> orders;

    private TradeState tradeState;

    private boolean isPermanent;

    public Long getUid() {
        return uid;
    }

    void setUid(Long uid) {
        this.uid = uid;
    }

    public Set<TradeOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<TradeOrder> orders) {
        this.orders = orders;
    }

    public TradeState getTradeState() {
        return tradeState;
    }

    public void setTradeState(TradeState tradeState) {
        this.tradeState = tradeState;
    }

    public boolean getIsPermanent() {
        return isPermanent;
    }

    public void setIsPermanent(boolean permanent) {
        isPermanent = permanent;
    }
}