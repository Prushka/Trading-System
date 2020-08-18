package phase2.trade.trade;

import phase2.trade.user.User;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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

    public Set<TradeOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<TradeOrder> orders) {
        this.orders = orders;
    }

    // A trade should contain only one pair of such users
    public TradeOrder findOrderByUserPair(User a, User b) {
        for (TradeOrder tradeOrder : orders) {
            if (tradeOrder.ifUserPairMatchOrder(a, b)) return tradeOrder;
        }
        return null;
    }

    public Collection<TradeOrder> findOrdersContainingUser(User user) {
        Collection<TradeOrder> tradeOrders = new HashSet<>();
        for (TradeOrder tradeOrder : orders) {
            if (tradeOrder.ifUserInOrder(user)) tradeOrders.add(tradeOrder);
        }
        return tradeOrders;
    }

    public boolean ifUserPairInOrder(User a, User b) {
        return findOrderByUserPair(a, b) != null;
    }

    public Iterator<UserOrderBundle> userOrderBundleIterator() {
        return new TradeUserOrderBundleIterator(this);
    }

}