package phase2.trade.trade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TradeOrder> orders = new HashSet<>();

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

}